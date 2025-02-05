# Step 1.1: Create ReportingStructure Class

## Iteration 1
## Implementation Details
- Created ReportingStructure class in data package
- Added two fields: employee and numberOfReports
- Implemented basic constructor and getters
- Used direct Employee reference instead of just ID for easier access



# Step 1.2: Implement REST Endpoint and Service Layer

## Iteration 1
## Implementation Details
 - Created ReportingStructureController with GET endpoint
 - Implemented ReportingStructureService interface
 - Added ServiceImpl with recursive counting logic
 - Used Set to prevent infinite loops in circular references
 - Endpoint attempts to follow existing API patterns
 - Service layer designed to separate business logic from controller
 - Using the recursive implementation handles nested reporting structures
 - Likewise, added safety against circular references
