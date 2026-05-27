export const useLinks = () => {
  const links = [
    { name: 'Dashboard', to: '/' },
    { name: 'Projects', to: '/projects' },
    { name: 'Kanban', to: '/kanban' },
  ]
  return { links }
}
