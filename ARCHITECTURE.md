# CAIRO Framework Architecture

## 1. Component Hierarchy
CAIRO bypasses the browser's DOM entirely. Instead, we manage our own logical UI structure in memory using a strictly typed class hierarchy. The root of all visual elements is the abstract `Component` class.

```text
Component (Abstract)
│
├── Container (Abstract)
│   ├── Panel
│   └── ScrollPane
│
└── LeafComponent (Abstract)
    ├── Label
    ├── Button
    └── TextField