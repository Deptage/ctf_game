import {BrowserRouter, Routes, Route} from 'react-router-dom';
import BankPage from './BankPage';
import AccountPage from './AccountPage';
import RegisterPage from './RegisterPage';
import { NotFoundPage } from './NotFoundPage';
import SavePortAndRedirect from './SavePortAndRedirect';
const AppRoutes=()=>{
    return(
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<BankPage />} />
                <Route path="/account" element={<AccountPage />} />
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/start/:data" element={<SavePortAndRedirect />} />
                <Route path ='*' element={<NotFoundPage />} />
            </Routes>
        </BrowserRouter>
    )
}
export default AppRoutes;