# Tenant - State Machine

## States
- ACTIVE
- SUSPENDED
- DELETED

## Transitions
- ACTIVE -> SUSPENDED (Suspend)
- SUSPENDED -> ACTIVE (Reactivate)
- ACTIVE|SUSPENDED -> DELETED (Delete/Deactivate)

## Notes
- DELETED là terminal state.

