SELECT epsh.SEQUENCE_ID sequnceId,
epsh.STORE_ID storeId,
epsh.REGISTER_ID wkstnId,
epsh.BUSINESS_DATE businessDate,
epsh.TRANSACTION_ID transId,
epsh.INBOUND_ERP_RESPONSE postTransResponse,
epsh.INBOUND_POS_REQ posRequest
FROM EXTRA_POS_SERVICE_HANDLER epsh
WHERE NOT EXISTS 
  (SELECT 1
  FROM UEC_TR_LTM_TAQSEET_TNDR_DTL uec
  WHERE uec.ai_trn = epsh.TRANSACTION_ID
  AND uec.ID_WS    = epsh.REGISTER_ID
  AND uec.ID_STR_RT=epsh.STORE_ID
  AND uec.DC_DY_BSN=SUBSTR(epsh.BUSINESS_DATE,0,10)
  ) and epsh.SERVICE_NAME='PostTransaction'
  and UTL_RAW.CAST_TO_VARCHAR2(DBMS_LOB.SUBSTR(INBOUND_ERP_RESPONSE, 4000,1)) like '%"Status":"0"%'
  and (epsh.RCN_STATUS is null)
  and epsh.BUSINESS_DATE = TO_CHAR(trunc(sysdate),'YYYY-MM-DD')