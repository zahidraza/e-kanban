export function getProductId (cId,scId,pId) {
  let cIdStr = String(cId);
  let scIdStr = String(scId);
  let pIdStr = String(pId);

  if (cIdStr.length == 1) {
    cIdStr = "0" + cIdStr;
  }

  if (scIdStr.length == 1) {
    scIdStr = "00" + scIdStr;
  }else if (scIdStr.length == 2) {
    scIdStr = "0" + scIdStr;
  }

  let l = pIdStr.length;
  l = 5 - l;
  let prefix = '';
  for (var i = 0; i < l ; i++) {
    prefix = prefix + '0';
  }
  pIdStr = prefix + pIdStr;

  const result = 'P' + cIdStr + scIdStr + pIdStr;

  return result;

}

export function getBinId (cId,scId,pId,binNo) {
  let cIdStr = String(cId);
  let scIdStr = String(scId);
  let pIdStr = String(pId);
  let binStr = String(binNo);

  if (cIdStr.length == 1) {
    cIdStr = "0" + cIdStr;
  }

  if (scIdStr.length == 1) {
    scIdStr = "00" + scIdStr;
  }else if (scIdStr.length == 2) {
    scIdStr = "0" + scIdStr;
  }

  let l = pIdStr.length;
  l = 5 - l;
  let prefix = '';
  for (var i = 0; i < l ; i++) {
    prefix = prefix + '0';
  }
  pIdStr = prefix + pIdStr;

  if (binStr.length == 1) {
    binStr = "0" + binStr;
  }

  const result = 'B' + cIdStr + scIdStr + pIdStr + binStr;

  return result;

}

export function getNoOfBins(bins) {
  return Math.ceil(bins.length/2);
}

export function getAgeing (createdAt) {
  const today = new Date().getTime();
  const durationHours = Math.ceil((today - createdAt)/(60*60*1000));
  console.log(durationHours);
  const daySuffix = (durationHours < 36) ? ' day' : ' days';
  let durationDays = Math.floor(durationHours/24);
  if (durationHours%24 > 12) {
    durationDays++;
  }
  return String(durationDays) + daySuffix;
}

export function getMonth(month) {

  switch (month) {
    case 1: return 'jan';
      break;
    case 2: return 'feb';
      break;
    case 3: return 'mar';
      break;
    case 4: return 'apr';
      break;
    case 5: return 'may';
      break;
    case 6: return 'jun';
      break;
    case 7: return 'jul';
      break;
    case 8: return 'aug';
      break;
    case 9: return 'sep';
      break;
    case 10: return 'oct';
      break;
    case 11: return 'nov';
      break;
    case 12: return 'dec';
      break;
  }
}

export function getCurrYear() {
  return new Date().getFullYear();
}

export function getCurrMonth() {
  return new Date().getMonth()+1;
}

export function getItemMasterHeader() {
  let productsDownload = ['Product Id','Item Code','Product Name','Category','Sub Category','Sections','Price','Ordering Time','Production Time',
    'Transportation Time','Buffer Time','UOM Purchase','UOM Consumption','Conversion Factor','MOQ','Packet Size'];
  for (var i = 0, j = getCurrMonth() + 1; i < 12; i++) {
    productsDownload.push(getMonth(j));
    j++;
    if (j == 13) {
      j = 1;
    }
  }
  productsDownload.push('Average');
  productsDownload.push('Max');
  productsDownload.push('Monthly Consumption Value');
  productsDownload.push('Class Type');
  productsDownload.push('Current Inventory');
  productsDownload.push('Demand');
  productsDownload.push('No of Bins');
  productsDownload.push('Bin Size');
  productsDownload.push('Kanban Type');

  return productsDownload;
}

export function getItemMasterBody(p) {
  console.log('getItemMasterBody');
  let sections = '  ';
  p.sectionList.forEach(s => sections += s.name + ', ');
  sections = sections.substring(0,sections.length-2).trim();

  let body = [p.productId,p.itemCode,p.name,p.category.name,p.subCategory.name,sections,p.price,p.timeOrdering,p.timeProcurement,
    p.timeTransporation,p.timeBuffer,p.uomPurchase,p.uomConsumption,p.conversionFactor,
    p.minOrderQty,p.packetSize];
  let sum = 0, max = 0, count = 0,value;
  for (var i = 0, j = getCurrMonth() + 1; i < 12; i++) {
    value = p[getMonth(j)];
    if (value != undefined) {
      max = value > max ? value : max;
      sum += value;
      count++;
      body.push(value);
    } else {
      body.push('');
    }
    j++;
    if (j == 13) {
      j = 1;
    }
  }
  console.log(max);
  console.log(p.price);
  const avg = Math.ceil(sum/count);
  body.push(avg);
  body.push(max);
  body.push(Math.ceil(avg*p.price));
  body.push(p.classType);
  body.push(p.stkOnFloor);
  body.push(p.demand);
  body.push(p.noOfBins);
  body.push(p.binQty);
  body.push(p.kanbanType);

  return body;
}
