# CommonConfig Usage

The `CommonConfig` class provides the basis for all Configurations.  It provides a concrete example of how to merge Fragments into a config.  The `CommonConfig` provides a simple mechanism to validate that parameters are correct and to calculate complex default values.

## Validating parameters are correct

The CommonConfig constructor requires a CommonConfigDef argument.  The constructor will call the `CommonConfigDef.multiValidate()` method.  This calls the `ConfigFragment.validate()` methods, providing the fragments the opportunity to detect conflicting arguments.  If there are any issues detected during the `multiValidate()` and exception will be thrown with detailed descriptions of each of the issues.

See [ConfigFragment Usage validation documentation](./ConfigFramgmentUsage.html#Validation) for details.

## Calculating complex default values

In some cases default values can only be determined by looking at other properties.  To support this operation `CommonConfig` implementations should implement the `fragmentPostProcess(ChangeTrackingMap map)` method to call the `postProcess(CommonConfig.ChangeTrackingMap configMap)` methods on embedded Fragments. This will allow the Fragments to properly set the default values before validation is checked.

