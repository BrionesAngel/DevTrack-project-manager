export const useLinks = () => {
  const links = [
    { name: 'Dashboard', to: '/' },
    { name: 'Projects', to: '/projects' },
    { name: 'Teams', to: '/teams' },
    { name: 'Docs', to: '/docs' },
    { name: 'Kanban', to: '/kanban' },
  ]
  return { links }
}
