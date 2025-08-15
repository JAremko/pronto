# Protobuf v4 Migration Guide

## Overview
This document describes the changes required when upgrading from Protobuf v3 to v4 in the Pronto library.

## Key Changes

### Oneof Field Behavior
The most significant change in Protobuf v4 is the correct handling of `oneof` fields according to the Protobuf specification.

#### Previous Behavior (Protobuf v3.x)
- Unset `oneof` fields incorrectly returned default values
- Example: An unset enum field in a `oneof` would return the first enum value (e.g., `:LOW`)
- Example: An unset string field in a `oneof` would return an empty string `""`
- Example: An unset numeric field in a `oneof` would return `0`

#### Current Behavior (Protobuf v4.x)
- Unset `oneof` fields correctly return `nil` (or `None`/`NOT_SET` in other languages)
- This aligns with the official Protobuf specification
- Only explicitly set fields have values, even if set to the default value

### Code Changes Required

#### Tests
Tests that expected default values for unset `oneof` fields need to be updated:

```clojure
;; Before (incorrect expectation)
(is (= {:level :LOW} 
       (-> (p/proto-map mapper People$Person)
           (p/proto-map->clj-map p/remove-default-values-xf))))

;; After (correct expectation)
(is (= {} 
       (-> (p/proto-map mapper People$Person)
           (p/proto-map->clj-map p/remove-default-values-xf))))
```

#### Application Code
If your application code relies on default values for unset `oneof` fields, you'll need to handle `nil` values explicitly:

```clojure
;; Before (assuming default values)
(let [level (:level person-map)]  ; Could be :LOW even if unset
  (process-level level))

;; After (handling nil for unset fields)
(let [level (or (:level person-map) :LOW)]  ; Explicitly provide default if nil
  (process-level level))
```

### Detecting Oneof Field State
To check if a `oneof` field is set:

```clojure
;; Check which field in a oneof is set
(p/which-one-of proto-map :thing)  ; Returns keyword of set field or nil

;; Get the value of the set field in a oneof
(p/one-of proto-map :thing)  ; Returns value or nil if no field is set
```

## Java Class Changes
- `GeneratedMessageV3` has been replaced with `GeneratedMessage`
- This is handled transparently by the Pronto library

## Summary
The Protobuf v4 upgrade brings the library into compliance with the official Protobuf specification. The main impact is on `oneof` field handling, where unset fields now correctly return `nil` instead of default values. This is a breaking change that may require updates to test expectations and application code that assumes default values for unset `oneof` fields.