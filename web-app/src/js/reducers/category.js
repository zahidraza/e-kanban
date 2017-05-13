import {CATEGORY_CONSTANTS as c, SUB_CATEGORY_CONSTANTS as sc, PRODUCT_CONSTANTS as p} from "../utils/constants";
import {getProductId} from "../utils/miscUtil";

const initialState = {
  categories:[],
  busy: false,

  addingCategory: false,
  editingCategory: false,
  errorCategory: {},

  addingSubCategory: false,
  editingSubCategory: false,
  errorSubCategory: {},

  addingProduct: false,
  editingProduct: false,

  products:[],
  product: {},  //Product being edited
  uploading: false,
  uploaded: false, 
  refreshing: false,
  errorProduct: {},
  errorsProduct: [],
  filter: {
    subCategory: [],
    section: []
  },
  sort: 'category:asc',
  toggleStatus: true
};

const handlers = {
//////////////////////////////////////// Category ////////////////////////////////////////// 
  [c.INITIALIZE_CATEGORY]: (_, action) => {
    const categories = action.payload.categories;
    let products = [] ;
    categories.forEach(c => {
      c.subCategoryList.forEach(sc => {
        sc.productList.forEach(p => {
          products.push({...p, subCategory: {id: sc.id, name: sc.name}, category: {id: c.id, name: c.name}, productId: getProductId(c.id,sc.id,p.id)});
        });
      });
    });
    return ({categories, products, toggleStatus: !_.toggleStatus});
  },
  [c.CATEGORY_BAD_REQUEST]: (_, action) => {
    let errorCategory = {};
    action.payload.errors.forEach(err => {
      errorCategory[err.field] = err.message;
    });
    return ({errorCategory, busy: false});
  },
  [c.CATEGORY_ADD_FORM_TOGGLE]: (_, action) => ({addingCategory: action.payload.adding, busy: false, errorCategory: {}}),
  [c.CATEGORY_ADD_PROGRESS]: (_, action) => ({busy: true, errorCategory: {}}),
  [c.CATEGORY_ADD_SUCCESS]: (_, action) => {
    let categories = _.categories;
    categories.push(action.payload.category);
    return ({addingCategory: false,toggleStatus: !_.toggleStatus, categories: categories, busy: false});
  },
  [c.CATEGORY_ADD_FAIL]: (_, action) => ({addingCategory: false, busy: false}),
  [c.CATEGORY_EDIT_FORM_TOGGLE]: (_, action) => ({editingCategory: action.payload.editing, busy: false, errorCategory: {}}),
  [c.CATEGORY_EDIT_PROGRESS]: (_, action) => ({busy: true,errorCategory: {}}),
  [c.CATEGORY_EDIT_SUCCESS]: (_, action) => {
    let categories = _.categories;
    let i = categories.findIndex(e=> e.id == action.payload.category.id);
    categories[i] = action.payload.category;
    return ({editingCategory: false,toggleStatus: !_.toggleStatus, categories: categories, busy: false});
  },
  [c.CATEGORY_EDIT_FAIL]: (_, action) => ({editingCategory: false, busy: false}),
  [c.CATEGORY_REMOVE_SUCCESS]: (_, action) => {
    let categories = _.categories.filter((c)=> c.id != action.payload.category.id);
    return ({toggleStatus: !_.toggleStatus,categories: categories});
  },
  [c.CATEGORY_FILTER]: (_, action) => ({filter: action.payload.filter, toggleStatus: !_.toggleStatus}),
  [c.CATEGORY_SORT]: (_, action) => ({sort: action.payload.sort, toggleStatus: !_.toggleStatus}),

//////////////////////////////////////////////// Sub Category /////////////////////////////////////////////////////////
  [sc.SUB_CATEGORY_BAD_REQUEST]: (_, action) => {
    let errorSubCategory = {};
    action.payload.errors.forEach(err => {
      errorSubCategory[err.field] = err.message;
    });
    return ({errorSubCategory, busy: false});
  },
  [sc.SUB_CATEGORY_ADD_FORM_TOGGLE]: (_, action) => ({addingSubCategory: action.payload.adding, busy: false, errorSubCategory: {}}),
  [sc.SUB_CATEGORY_ADD_PROGRESS]: (_, action) => ({busy: true, errorSubCategory: {}}),
  [sc.SUB_CATEGORY_ADD_SUCCESS]: (_, action) => {
    const subCategory = action.payload.subCategory;
    let categories = _.categories;
    let i = categories.findIndex(c=> c._links.self.href === subCategory._links.category.href);
    let category = categories[i];
    category.subCategoryList.push(subCategory);
    categories[i] = category;
    return ({addingSubCategory: false, toggleStatus: !_.toggleStatus, categories: categories, busy: false});
  },
  [sc.SUB_CATEGORY_ADD_FAIL]: (_, action) => ({addingSubCategory: false, busy: false}),
  [sc.SUB_CATEGORY_EDIT_FORM_TOGGLE]: (_, action) => ({editingSubCategory: action.payload.editing, busy: false, errorSubCategory: {}}),
  [sc.SUB_CATEGORY_EDIT_PROGRESS]: (_, action) => ({busy: true, errorSubCategory: {}}),
  [sc.SUB_CATEGORY_EDIT_SUCCESS]: (_, action) => {
    const subCategory = action.payload.subCategory;
    let categories = _.categories;
    let i = categories.findIndex(c=> c._links.self.href === subCategory._links.category.href);
    let category = categories[i];
    let j = category.subCategoryList.findIndex(sc => sc.id === subCategory.id);
    category.subCategoryList[j] = subCategory;
    categories[i] = category;
    return ({editingSubCategory: false,toggleStatus: !_.toggleStatus, categories: categories, busy: false});
  },
  [sc.SUB_CATEGORY_EDIT_FAIL]: (_, action) => ({editingSubCategory: false, busy: false}),
  [sc.SUB_CATEGORY_REMOVE_SUCCESS]: (_, action) => {
    const subCategory = action.payload.subCategory;
    let categories = _.categories;
    let i = categories.findIndex(c=> c._links.self.href === subCategory._links.category.href);
    let category = categories[i];
    category.subCategoryList = category.subCategoryList.filter(sc => sc.id != subCategory.id);
    categories[i] = category;
    return ({toggleStatus: !_.toggleStatus,categories: categories});
  },
  //////////////////////////////////////////////// Product /////////////////////////////////////////////////////////
  [p.PRODUCT_BAD_REQUEST]: (_, action) => {
    let errorProduct = {};
    action.payload.errors.forEach(err => {
      errorProduct[err.field] = err.message;
    });
    return ({errorProduct, busy: false});
  },
  [p.PRODUCT_ADD_FORM_TOGGLE]: (_, action) => ({addingProduct: action.payload.adding, busy: false, errorProduct: {}}),
  [p.PRODUCT_ADD_PROGRESS]: (_, action) => ({busy: true, errorProduct: {}}),
  [p.PRODUCT_ADD_SUCCESS]: (_, action) => {
    let product = action.payload.product;
    product = {...product, productId: getProductId(product.category.id,product.subCategory.id,product.id)};
    let products = _.products;
    products.push(product);
    let categories = _.categories;
    const i = categories.findIndex(c=> c.id === product.category.id);
    const j = categories[i].subCategoryList.findIndex(sc=> sc.id === product.subCategory.id);
    let subCategory = categories[i].subCategoryList[j];
    subCategory.productList.push(product);
    categories[i].subCategoryList[j] = subCategory;
    return ({addingProduct: false, busy: false, toggleStatus: !_.toggleStatus, categories: categories});
  },
  [p.PRODUCT_ADD_FAIL]: (_, action) => ({addingProduct: false, busy: false}),
  [p.PRODUCT_SYNC_PROGRESS]: (_, action) => ({refreshing: true}),
  [p.PRODUCT_SYNC_SUCCESS]: (_, action) => ({refreshing: false, uploaded: false}),
  [p.PRODUCT_UPLOAD_FORM_TOGGLE]: (_, action) => ({uploading: action.payload.uploading}),
  [p.PRODUCT_UPLOAD_SUCCESS]: (_, action) => ({uploading: false, busy: false, errorsProduct: [], uploaded: true}),
  [p.PRODUCT_UPLOAD_PROGRESS]: (_, action) => ({busy: true, errorsProduct: []}),
  [p.PRODUCT_UPLAOD_ERROR]: (_, action) => ({busy: false, errorsProduct: action.payload.errors}),
  [p.PRODUCT_UPLAOD_FAIL]: (_, action) => ({uploading: false, busy: false}),
  [p.PRODUCT_EDIT_FORM_TOGGLE]: (_, action) => ({editingProduct: action.payload.editing, product: action.payload.product, busy: false, errorProduct: {}}),
  [p.PRODUCT_EDIT_PROGRESS]: (_, action) => ({busy: true, errorProduct: {}}),
  [p.PRODUCT_EDIT_SUCCESS]: (_, action) => {
    const product = action.payload.product;
    let categories = _.categories;
    let products = _.products;
    const x = products.findIndex(p => p.id === product.id);
    products[x] = product;
    const i = categories.findIndex(c=> c.id === product.category.id);
    const j = categories[i].subCategoryList.findIndex(sc=> sc.id === product.subCategory.id);
    const k = categories[i].subCategoryList[j].productList.findIndex(p=> p.id === product.id);
    categories[i].subCategoryList[j].productList[k] = product;
    
    return ({editingProduct: false, busy: false,toggleStatus: !_.toggleStatus, categories, products});
  },
  [p.PRODUCT_EDIT_FAIL]: (_, action) => ({editingProduct: false, busy: false}),
  [p.PRODUCT_REMOVE_SUCCESS]: (_, action) => {
    const product = action.payload.product;
    let categories = _.categories;
    const i = categories.findIndex(c=> c.id === product.category.id);
    const j = categories[i].subCategoryList.findIndex(sc=> sc.id === product.subCategory.id);
    categories[i].subCategoryList[j].productList = categories[i].subCategoryList[j].productList.filter(p => p.id != product.id);
    return ({toggleStatus: !_.toggleStatus,categories: categories});
  }
};

export default function category (state = initialState, action) {
  let handler = handlers[action.type];
  if( !handler ) return state;
  return { ...state, ...handler(state, action) };
}
