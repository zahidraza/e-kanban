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
