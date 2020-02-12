package com.reconcile;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author RetailOMatrix This class is created to start the reconciliation
 *         batch.
 *
 */
public class TaqseetReconcileUtillity {

	static Logger logger = Logger.getLogger(TaqseetReconcileUtillity.class);

	public static void main(String[] batchParam) {

		configureLog4j();
		
		BatchParameter batchType = null;

		if (batchParam.length > 0) {

			if (batchParam[0].equalsIgnoreCase("1")) {

				batchType = BatchParameter.GENERATE_REPORT;

			} else if (batchParam[0].equalsIgnoreCase("2")) {

				batchType = BatchParameter.TRANSACTION_REVERSAL;
			} else {
				logger.error("Invalid Or No paramters passed. Valid parameters are: "
						+ BatchParameter.GENERATE_REPORT.getCode() + "-"
						+ BatchParameter.GENERATE_REPORT.getDescription() + ", "
						+ BatchParameter.TRANSACTION_REVERSAL.getCode() + "-"
						+ BatchParameter.TRANSACTION_REVERSAL.getDescription());
			}

		} else {
			logger.error("Invalid Or No paramters passed. Valid parameters are: "
					+ BatchParameter.GENERATE_REPORT.getCode() + "-" + BatchParameter.GENERATE_REPORT.getDescription()
					+ ", " + BatchParameter.TRANSACTION_REVERSAL.getCode() + "-"
					+ BatchParameter.TRANSACTION_REVERSAL.getDescription());
		}

		if (batchType != null && batchType.compareTo(BatchParameter.GENERATE_REPORT) == 0) {

			logger.info(batchType.getDescription() + ": Process Started....");
			DBManager dbManager = DBManager.getInstance();
			List<CancelledTransactionDAO> cancelledTrans = dbManager.getMissedCancelledTransactionByTrans();
			if (cancelledTrans.size() == 0) {
				logger.info("No Transaction Found.");

			} else {
				PosServiceUtil service = new PosServiceUtil();
				service.generateMissingTransFile(cancelledTrans);
			}
			logger.info(batchType.getDescription() + ": Process Completed.");
			
		} else if (batchType != null && batchType.compareTo(BatchParameter.TRANSACTION_REVERSAL) == 0) {
			logger.info(batchType.getDescription() + ": Process Started....");
			PosServiceUtil service = new PosServiceUtil();
			List<CancelledTransactionDAO> transToReverse = service.getCancelledTransDetails();
			if (transToReverse.size() == 0) {
				logger.info("No Transaction Found.");

			} else {
				 processReverseTransctions(transToReverse);
			}
			logger.info(batchType.getDescription() + ": Process Completed.");

		}
	}

	private static void configureLog4j() {

		BasicConfigurator.configure();
		Properties prop = new Properties();
		try {
			prop.load(PosServiceUtil.class.getClass().getResourceAsStream("/log4j.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		PropertyConfigurator.configure(prop);
	}

	private static void processReverseTransctions(List<CancelledTransactionDAO> cancelledTrans) {

		PosServiceUtil service = new PosServiceUtil();
		for (CancelledTransactionDAO cancelledTransaction : cancelledTrans) {
			boolean transactionStatus = service.getTransactionStatus(cancelledTransaction.getRetailRefNo());
			if (transactionStatus) {
				service.callPostTransactionReversal(cancelledTransaction);
			}
		}
	}
}
