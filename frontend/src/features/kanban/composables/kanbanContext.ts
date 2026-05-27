export interface KanbanContext {
  projectId: number
  teamId?: number
  userId?: number
}

const KANBAN_CONTEXT_KEY = 'last-kanban-context'

export function loadKanbanContext(): KanbanContext | null {
  if (typeof window === 'undefined') return null

  const raw = window.localStorage.getItem(KANBAN_CONTEXT_KEY)
  if (!raw) return null

  try {
    const parsed = JSON.parse(raw) as KanbanContext
    if (!Number.isFinite(parsed.projectId) || parsed.projectId <= 0) return null
    return parsed
  } catch {
    return null
  }
}

export function saveKanbanContext(context: KanbanContext) {
  if (typeof window === 'undefined') return
  window.localStorage.setItem(KANBAN_CONTEXT_KEY, JSON.stringify(context))
}

export function clearKanbanContext() {
  if (typeof window === 'undefined') return
  window.localStorage.removeItem(KANBAN_CONTEXT_KEY)
}
