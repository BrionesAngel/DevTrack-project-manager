# DevTrack Frontend

DevTrack is a frontend prototype for project, team, and task management using a feature-based architecture.
It is currently focused on UI structure and navigation, with a conceptual flow for task creation and tracking.

## Stack

- Vue 3 + TypeScript
- Vite
- Vue Router
- Pinia
- Tailwind CSS

## Scripts

```sh
npm install
npm run dev
npm run type-check
npm run lint
npm run build
```

## Estructura del proyecto (simple)
## Project structure (simple)

```text
src/
  features/
    projects/   # Project views and routes
    tasks/      # Task views, routes, and components
    kanban/     # Board and tracking views
    teams/      # Team views and components
    docs/       # Documentation view and components
  shared/
    components/ # Global reusable components
  router/
    index.ts    # Main router and feature route registration
  composables/
    useLinks.ts # Main navigation links
  stores/
    counter.ts  # Pinia store example
  assets/
    main.css    # Base styles
```

## Current status

- Feature-based modular structure is implemented.
- Main navigation includes Dashboard, Projects, Tasks, Teams, Docs, and Kanban.
- Tasks are available through a global route and a project-context flow.
