import React from 'react'
import Dashboard from '../components/Dashboard'
import useUser from '../hooks/useUser'

function Filter() {
  useUser();
  return (
    <Dashboard activeMenu="Filter">
      This is the filter
    </Dashboard>
  )
}

export default Filter