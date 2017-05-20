import Dashboard from "./components/Dashboard";
import Main from "./components/Main";
import Login from "./components/Login";
import Profile from "./components/Profile";
import Reports from './components/Reports';

import Category from "./components/admin/category/Category";
import Product from "./components/admin/product/Product";
import ProductAdd from "./components/admin/product/ProductAdd";
import ProductUpload from "./components/admin/product/ProductUpload";
import ProductEdit from "./components/admin/product/ProductEdit";
import Section from "./components/admin/section/Section";
import SubCategory from "./components/admin/subCategory/SubCategory";
import Supplier from "./components/admin/supplier/Supplier";
import SupplierAdd from "./components/admin/supplier/SupplierAdd";
import SupplierEdit from "./components/admin/supplier/SupplierEdit";
import User from "./components/admin/user/User";

import Stock from './components/store/Stock';
import InwardScan from "./components/store/InwardScan";
import OutwardScan from "./components/store/OutwardScan";
import BarcodeGenerate from "./components/store/BarcodeGenerate";
import AwaitingOrder from './components/store/AwaitingOrder';

import Tracking from './components/purchase/Tracking';
import Order from './components/purchase/Order';
import Followup from './components/purchase/Followup';
import Test from "./components/Test";

export default {
  path: '/',
  component: Main,
  indexRoute: {component: Login},
  childRoutes: [
    { path: 'dashboard', component: Dashboard},
    { path: 'profile', component: Profile},
    { path: 'reports', component: Reports},

    { path: 'category', component: Category},
    { path: 'product', component: Product},
    { path: 'product/add', component: ProductAdd},
    { path: 'product/upload', component: ProductUpload},
    { path: 'product/edit', component: ProductEdit},
    { path: 'section', component: Section},
    { path: 'subCategory', component: SubCategory},
    { path: 'supplier', component: Supplier},
    { path: 'supplier/add', component: SupplierAdd},
    { path: 'supplier/edit', component: SupplierEdit},
    { path: 'user', component: User},

    { path: 'stock', component: Stock},
    { path: 'inward', component: InwardScan},
    { path: 'outward', component: OutwardScan},
    { path: 'barcode', component: BarcodeGenerate},
    { path: 'awaiting', component: AwaitingOrder},

    { path: 'tracking', component: Tracking},
    { path: 'order', component: Order},
    { path: 'followup', component: Followup},
    { path: 'test', component: Test}
  ]
};
