import React, { useContext } from 'react';
import Menubar from './Menubar';
import { AppContext } from '../context/AppContext';
import Sidebar from './Sidebar';

function Dashboard({children,activeMenu}) {
  const { user } = useContext(AppContext);

  return (
    <div>
      <Menubar activeMenu={activeMenu} />

        {user && (
          <div className="flex">
          <div className="hidden lg:block">
            <Sidebar activeMenu={activeMenu}/>
          </div>

          <div className="grow mx-5">
           {children}
          </div>
        </div>
        )}
    
    </div>
  );
}

export default Dashboard;
