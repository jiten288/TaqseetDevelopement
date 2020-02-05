drop table EXTRA_POS_SERVICE_HANDLER;
CREATE TABLE EXTRA_POS_SERVICE_HANDLER
  (
    sequence_id      NUMBER PRIMARY KEY,
    session_id       VARCHAR2(250) ,
    store_id         VARCHAR2(50),
    register_id      VARCHAR2(50),
    transaction_id   NUMBER,
    business_date VARCHAR2(50),
    service_name     VARCHAR2(30),
    inbound_pos_req BLOB,
    outbound_pos_response BLOB,
    outbound_erp_req BLOB,
    inbound_erp_response BLOB,
    create_date TIMESTAMP,
    update_date TIMESTAMP
  );
  
  