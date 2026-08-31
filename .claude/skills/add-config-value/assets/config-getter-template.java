// Add to application.conf (and env-specific overrides if needed):
//
//   {section} {
//     {key} = "{default-value}"
//     {key} = ${?{ENV_VAR_NAME}}
//   }

// Add to ConfigManager.java, next to the other public accessors:

/** @return {Description of what this value controls} */
public static String get{ValueName}() {
    return getStringConfig("{section}.{key}", "{hardcoded-default}");
}

// For an int-typed value:
public static int get{ValueName}() {
    return getIntConfig("{section}.{key}", {hardcoded-default});
}

// For a boolean-typed value, optionally with a system-property/env override
// ahead of the config file (mirror isHeadless() only if a CLI override is
// genuinely required):
public static boolean is{ValueName}() {
    String envVal = System.getenv("{ENV_VAR_NAME}");
    if (envVal != null && !envVal.isBlank()) {
        return Boolean.parseBoolean(envVal.trim());
    }
    return getBooleanConfig("{section}.{key}", {hardcoded-default});
}
