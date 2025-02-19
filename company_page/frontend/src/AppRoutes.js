import {BrowserRouter, Routes, Route} from 'react-router-dom';
import CompanyPage from './CompanyPage';
import { NotFoundPage } from './NotFoundPage';
import WorkersPage from './WorkersPage';
import SavePortAndRedirect from './SavePortAndRedirect';
import AdminPanel from './AdminPanel';
const AppRoutes=()=>{
    return(
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<CompanyPage />} />
                <Route path="/get-to-know-us" element={<WorkersPage/>} />
                <Route path="/start/:data" element={<SavePortAndRedirect />} />
                <Route path="/adminpanel" element={<AdminPanel/>}/>
                <Route path ='*' element={<NotFoundPage />} />
            </Routes>
        </BrowserRouter>
    )
}
export default AppRoutes;
