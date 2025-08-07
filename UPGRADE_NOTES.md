# Pronto Library Update for Protobuf v4 and buf.validate Support

## Overview
This document outlines the changes needed to update the Pronto library to work with:
- Protobuf Java v4.29.2 (from v3.15.0)
- Java proto bindings with buf.validate annotations
- New package structure from protogen

## Key Changes Required

### 1. Update Protobuf Dependency
The library needs to update from protobuf-java 3.15.0 to 4.29.2 to match the generated code.

### 2. Handle RuntimeVersion Validation
Protobuf v4 includes runtime version validation that ensures the generated code matches the runtime library version.

### 3. Support buf.validate Annotations
The new generated code includes buf.validate field annotations that appear as field options in the generated Java code. While these don't affect the basic proto operations, they're preserved in the generated code.

### 4. Package Structure
The new generated code uses simple package names (`cmd`, `ser`) instead of fully qualified packages.

## Implementation Steps

### Step 1: Update project.clj
```clojure
(def protobuf-version "4.29.2")  ; Update from 3.15.0
```

### Step 2: Test Compatibility
The core Pronto functionality should work with the new protobuf version since:
- The basic Message/Builder API remains the same
- Field descriptors and reflection APIs are backward compatible
- The main change is the addition of RuntimeVersion validation

### Step 3: Handle Optional Fields
Protobuf v4 has better support for optional fields via `hasOptionalKeyword()` which Pronto already uses.

### Step 4: Validation Support (Optional)
If you want to add validation support:
1. Add protovalidate-java as a dependency
2. Create validation wrappers that can validate proto-maps using the buf.validate annotations

## Testing

Create test cases using the new generated Java classes from protogen:
```clojure
(import 'cmd.JonSharedCmd$Root)
(import 'ser.JonSharedData$JonGUIState)

(defmapper mapper [cmd.JonSharedCmd$Root])
```

## Backward Compatibility

To maintain backward compatibility:
1. Keep support for protobuf v3 generated classes
2. Use reflection to detect version-specific features
3. Provide configuration options for version-specific behavior

## Validation Integration (Future Enhancement)

For buf.validate support, consider adding:
```clojure
(require '[pronto.validate :as v])

; Validate a proto-map against its buf.validate constraints
(v/validate proto-map)
```

This would use the protovalidate-java library to perform runtime validation.