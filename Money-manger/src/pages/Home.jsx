import React from 'react'
import Dashboard from '../components/Dashboard'
import useUser from '../hooks/useUser'

function Home() {
  useUser();
  return (
    <div>
      <Dashboard activeMenu="Dashboard">
        This is home page
      </Dashboard>
    </div>
  )
}

export default Home