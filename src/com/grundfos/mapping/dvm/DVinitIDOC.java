package com.grundfos.mapping.dvm;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.value.api.IFIdentifier;
import com.sap.aii.mapping.value.api.XIVMFactory;
import com.sap.aii.mapping.value.api.XIVMService;
import com.sap.aii.mappingtool.tf7.rt.Container;
import com.sap.aii.mappingtool.tf7.rt.GlobalContainer;

public class DVinitIDOC {

	public static String executeDVinitIDOC(String initTable, String direction, String standard, String message,
			String version, String partnerType, String partner, String company, Container container)
			throws StreamTransformationException {

		AbstractTrace trace = container.getTrace();

		String delimiter = "~@#~";
		String senderAgency = "VMR_Key";
		String senderScheme = "VMR_Source";
		String receiverAgency = "VMR_Return";
		String receiverScheme = "VMR_Target";
		String receiverScheme1 = "VMR_Target_1";
		String receiverScheme2 = "VMR_Target_2";
		String receiverScheme3 = "VMR_Target_3";
		String context = "";

		GlobalContainer globalContainer;
		globalContainer = container.getGlobalContainer();

		// Get VM set
		try {

			IFIdentifier vmsetsrc = XIVMFactory.newIdentifier("http://janro.com/vmrset", senderAgency, senderScheme);
			IFIdentifier vmsetdst = XIVMFactory
					.newIdentifier("http://janro.com/vmrset", receiverAgency, receiverScheme);
			String vmset = XIVMService.executeMapping(vmsetsrc, vmsetdst, "0.0.VMRSET");
			context = "http://janro.com/vmr/" + vmset;
			trace.addInfo("Class DVinitIDOC: VMR Set: " + context);

		} catch (Exception e) {

			return ("No ValueMapping found for " + "0.0.VMRSET");

			// trace.addInfo("Class DVinitIDOC: No ValueMapping found for " +
			// "0.0.VMRSET");
			// vmOut = new String[] { "", "", "", "", "", "" };

		}

		String vmKey = "";
		String vmReturn = "";
		String vmReturn1 = "";
		String vmReturn2 = "";
		String vmReturn3 = "";

		// Build VM prerequisites
		IFIdentifier source = XIVMFactory.newIdentifier(context, senderAgency, senderScheme);
		IFIdentifier destination1 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme1);
		IFIdentifier destination2 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme2);
		IFIdentifier destination3 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme3);

		try {

			/*-			vmkey = table + delimiter + direction + delimiter + standard + delimiter + message + delimiter + version
			 + delimiter + delimiter + delimiter + delimiter + partnertype + delimiter + partner + delimiter
			 + company + delimiter + delimiter + delimiter;
			 vmreturn = XIVMService.executeMapping(src, dst, vmkey);
			 String pc[] = vmreturn.split("\\" + delimiter);

			 String vmkeypc = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
			 + pc[0] + delimiter + pc[1] + delimiter + pc[2];
			 String vmkeypg = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
			 + "PG" + delimiter + pc[1] + delimiter;
			 String vmkeygc = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
			 + "GC" + delimiter + delimiter + pc[2];
			 String vmkeygg = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
			 + "GG" + delimiter + delimiter;

			 globalContainer.setParameter("vmkeypc", vmkeypc);
			 globalContainer.setParameter("vmkeypg", vmkeypg);
			 globalContainer.setParameter("vmkeygc", vmkeygc);
			 globalContainer.setParameter("vmkeygg", vmkeygg);
			 trace.addInfo("Initialize VM Key PC: " + vmkeypc);
			 trace.addInfo("Initialize VM Key PG: " + vmkeypg);
			 trace.addInfo("Initialize VM Key GC: " + vmkeygc);
			 trace.addInfo("Initialize VM Key GG: " + vmkeygg);*/

			/*
			 * Try first L1 initialization
			 */

			vmKey = initTable + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter
					+ delimiter + partnerType + delimiter + partner + delimiter + company + delimiter + delimiter
					+ delimiter;

			vmReturn1 = XIVMService.executeMapping(source, destination1, vmKey);
			try {
				// Check if VM Target has part 2
				vmReturn2 = XIVMService.executeMapping(source, destination2, vmKey);
			} catch (Exception evmReturn2) {
				vmReturn2 = "";
			}
			try {
				// Check if VM Target has part 3
				vmReturn3 = XIVMService.executeMapping(source, destination3, vmKey);
			} catch (Exception evmReturn3) {
				vmReturn3 = "";
			}

			vmReturn = vmReturn1 + vmReturn2 + vmReturn3;
			String L1[] = vmReturn.split("\\" + delimiter);

			String vmKeyL1 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ L1[0] + delimiter + L1[1] + delimiter + L1[2];
			String vmKeyL2 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "L2" + delimiter + L1[1] + delimiter;
			String vmKeyL3 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "L3" + delimiter + delimiter + L1[2];
			String vmKeyL4 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "L4" + delimiter + delimiter;

			globalContainer.setParameter("vmKeyL1", vmKeyL1);
			globalContainer.setParameter("vmKeyL2", vmKeyL2);
			globalContainer.setParameter("vmKeyL3", vmKeyL3);
			globalContainer.setParameter("vmKeyL4", vmKeyL4);

			trace.addInfo("Class DVinitIDOC: Initialize VM Key L1 - " + vmKeyL1);
			trace.addInfo("Class DVinitIDOC: Initialize VM Key L2 - " + vmKeyL2);
			trace.addInfo("Class DVinitIDOC: Initialize VM Key L3 - " + vmKeyL3);
			trace.addInfo("Class DVinitIDOC: Initialize VM Key L4 - " + vmKeyL4);

			return vmKeyL1;

		} catch (Exception eL1) {

			try {

				trace.addInfo("Class DVinitIDOC: No VM Key L1 found for " + vmKey + ". Trying VM Key L2.");

				/*-				vmkey = table + delimiter + direction + delimiter + standard + delimiter + message + delimiter
				 + version + delimiter + delimiter + delimiter + delimiter + partnertype + delimiter + partner
				 + delimiter + delimiter + delimiter + delimiter;
				 vmreturn = XIVMService.executeMapping(src, dst, vmkey);
				 String pg[] = vmreturn.split("\\" + delimiter);

				 String vmkeypc = ""; // direction + delimiter + standard +
				 // delimiter + message + delimiter +
				 // version + delimiter + pg[0] +
				 // delimiter + pg[1] + delimiter;
				 String vmkeypg = direction + delimiter + standard + delimiter + message + delimiter + version
				 + delimiter + "PG" + delimiter + pg[1] + delimiter;
				 String vmkeygc = direction + delimiter + standard + delimiter + message + delimiter + version
				 + delimiter + "GC" + delimiter + delimiter + company;
				 String vmkeygg = direction + delimiter + standard + delimiter + message + delimiter + version
				 + delimiter + "GG" + delimiter + delimiter;

				 globalContainer.setParameter("vmkeypc", vmkeypc);
				 globalContainer.setParameter("vmkeypg", vmkeypg);
				 globalContainer.setParameter("vmkeygc", vmkeygc);
				 globalContainer.setParameter("vmkeygg", vmkeygg);
				 trace.addInfo("Initialize VM Key PC: Not possible.");
				 trace.addInfo("Initialize VM Key PG: " + vmkeypg);
				 trace.addInfo("Initialize VM Key GC: " + vmkeygc);
				 trace.addInfo("Initialize VM Key GG: " + vmkeygg);
				 */

				vmKey = initTable + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter
						+ delimiter + partnerType + delimiter + partner + delimiter + company + delimiter + delimiter
						+ delimiter;

				vmReturn1 = XIVMService.executeMapping(source, destination1, vmKey);
				try {
					// Check if VM Target has part 2
					vmReturn2 = XIVMService.executeMapping(source, destination2, vmKey);
				} catch (Exception evmReturn2) {
					vmReturn2 = "";
				}
				try {
					// Check if VM Target has part 3
					vmReturn3 = XIVMService.executeMapping(source, destination3, vmKey);
				} catch (Exception evmReturn3) {
					vmReturn3 = "";
				}

				vmReturn = vmReturn1 + vmReturn2 + vmReturn3;
				String L2[] = vmReturn.split("\\" + delimiter);

				String vmKeyL1 = "";
				String vmKeyL2 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "L2" + delimiter + L2[1] + delimiter;
				String vmKeyL3 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "L3" + delimiter + delimiter + company;
				String vmKeyL4 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "L4" + delimiter + delimiter;

				globalContainer.setParameter("vmKeyL1", vmKeyL1);
				globalContainer.setParameter("vmKeyL2", vmKeyL2);
				globalContainer.setParameter("vmKeyL3", vmKeyL3);
				globalContainer.setParameter("vmKeyL4", vmKeyL4);

				trace.addInfo("Class DVinitIDOC: Initialize VM Key L1 not possible");
				trace.addInfo("Class DVinitIDOC: Initialize VM Key L2 - " + vmKeyL2);
				trace.addInfo("Class DVinitIDOC: Initialize VM Key L3 - " + vmKeyL3);
				trace.addInfo("Class DVinitIDOC: Initialize VM Key L4 - " + vmKeyL4);

				return vmKeyL2;

			} catch (Exception eL2) {

				globalContainer.setParameter("vmkeypc", "");
				globalContainer.setParameter("vmkeypg", "");
				globalContainer.setParameter("vmkeygc", "");
				globalContainer.setParameter("vmkeygg", "");
				globalContainer.setParameter("vmKeyL1", "");
				globalContainer.setParameter("vmKeyL2", "");
				globalContainer.setParameter("vmKeyL3", "");
				globalContainer.setParameter("vmKeyL4", "");

				trace.addInfo("Class DVinitIDOC: Failed to initialize map with VM Key: " + vmKey);

				return ("No VM Key PG found for " + vmKey);

			}

		}

	}

}
