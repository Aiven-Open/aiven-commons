/*
         Copyright 2025 Aiven Oy and project contributors

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing,
        software distributed under the License is distributed on an
        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
        KIND, either express or implied.  See the License for the
        specific language governing permissions and limitations
        under the License.

        SPDX-License-Identifier: Apache-2
 */

package io.aiven.commons.timing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Performs a delay based on the number of successive {@link #delay()} or
 * {@link #cleanDelay()} calls without a {@link #reset()}. Delay increases
 * exponentially but never exceeds the time remaining by more than 0.512
 * seconds.
 */
public class Backoff {
	/**
	 * The logger to write to
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Backoff.class);
	/**
	 * The maximum jitter random number. Should be a power of 2 for speed.
	 */
	public static final int MAX_JITTER = 1024;

	/**
	 * The value subtracted from the jitter to center it.
	 */
	public static final int JITTER_SUBTRAHEND = MAX_JITTER / 2;
	/**
	 * A supplier of the time remaining (in milliseconds) on the overriding timer.
	 */
	protected final SupplierOfLong timeRemaining;

	/**
	 * A function to call to abort the timer.
	 */
	protected final AbortTrigger abortTrigger;

	/**
	 * The maximum number of times {@link #delay()} will be called before maxWait is
	 * reached.
	 */
	private int maxCount;
	/**
	 * The number of times {@link #delay()} has been called.
	 */
	private int waitCount;
	/**
	 * If true then when wait count is exceeded {@link ##delay()} automatically
	 * returns without delay.
	 */
	private final boolean applyTimerRule;

	/**
	 * A random number generator to construct jitter.
	 */
	Random random = new Random();

	/**
	 * Constructor.
	 *
	 * @param config
	 *            The configuration for the backoff.
	 */
	public Backoff(final BackoffConfig config) {
		this.timeRemaining = config.getSupplierOfTimeRemaining();
		this.abortTrigger = config.getAbortTrigger();
		this.applyTimerRule = config.applyTimerRule();
		reset();
	}

	/**
	 * Reset the backoff time so that delay is again at the minimum.
	 */
	public final void reset() {
		// if the remaining time is 0 or negative the maxCount will be infinity
		// so make sure that it is 0 in that case.
		final long remainingTime = timeRemaining.get();
		maxCount = remainingTime < 1L ? 0 : (int) (Math.log10(remainingTime) / Math.log10(2));
		waitCount = 0;
		LOGGER.debug("Reset {}", this);
	}

	/**
	 * Handle adjustment when maxCount could not be set.
	 *
	 * @return the corrected maxCount
	 */
	private int getMaxCount() {
		if (maxCount == 0) {
			reset();
		}
		return maxCount;
	}

	/**
	 * Calculates the delay without jitter.
	 *
	 * @return the number of milliseconds the delay will be.
	 */
	public long estimatedDelay() {
		long sleepTime = timeRemaining.get();
		if (sleepTime > 0 && waitCount < maxCount) {
			sleepTime = (long) Math.min(sleepTime, Math.pow(2, waitCount + 1));
		}
		return sleepTime < 0 ? 0 : sleepTime;
	}

	/**
	 * Calculates the range of jitter in milliseconds.
	 *
	 * @return the maximum jitter in milliseconds. jitter is +/- maximum jitter.
	 */
	public int getMaxJitter() {
		return MAX_JITTER - JITTER_SUBTRAHEND;
	}

	private long timeWithJitter() {
		// generate approx +/- 0.512 seconds of jitter
		final int jitter = random.nextInt(MAX_JITTER) - JITTER_SUBTRAHEND;
		return (long) Math.pow(2, waitCount) + jitter;
	}

	/**
	 * If {@link #applyTimerRule} is true then this method will return false if the
	 * wait count has exceeded the maximum count. Otherwise it returns true. This
	 * method also increments the wait count if the wait count is less than the
	 * maximum count.
	 *
	 * @return true if sleep should occur.
	 */
	private boolean shouldSleep(final long sleepTime) {
		// maxcount may have been reset so check and set if necessary.
		final boolean result = sleepTime > 0
				&& (!applyTimerRule || waitCount < (maxCount == 0 ? getMaxCount() : maxCount));
		if (waitCount < maxCount) {
			waitCount++;
		}
		return result;
	}

	/**
	 * Delay execution based on the number of times this method has been called.
	 *
	 * @throws InterruptedException
	 *             If any thread interrupts this thread.
	 */
	public void delay() throws InterruptedException {
		final long sleepTime = timeRemaining.get();
		if (shouldSleep(sleepTime)) {
			final long nextSleep = timeWithJitter();
			// don't sleep negative time. Jitter can introduce negative tme.
			if (nextSleep > 0) {
				if (nextSleep >= sleepTime) {
					LOGGER.debug("Backoff aborting timer");
					abortTrigger.apply();
				} else {
					LOGGER.debug("Backoff sleeping {}", nextSleep);
					Thread.sleep(nextSleep);
				}
			}
		}
	}

	/**
	 * Like {@link #delay} but swallows the {@link InterruptedException}.
	 */
	@SuppressWarnings("PMD.EmptyCatchBlock")
	public void cleanDelay() {
		try {
			delay();
		} catch (InterruptedException expected) {
			// do nothing. We swallow the interruption so that we do not return an error.
		}
	}

	@Override
	public String toString() {
		return String.format("Backoff %s/%s, %s milliseconds remaining.", waitCount, maxCount, timeRemaining.get());
	}
}
