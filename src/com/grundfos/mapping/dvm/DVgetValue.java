package com.grundfos.mapping.dvm;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.value.api.IFIdentifier;
import com.sap.aii.mapping.value.api.ValueMappingException;
import com.sap.aii.mapping.value.api.XIVMFactory;
import com.sap.aii.mapping.value.api.XIVMService;
import com.sap.aii.mappingtool.tf7.rt.Container;
import com.sap.aii.mappingtool.tf7.rt.GlobalContainer;
import com.sap.aii.mappingtool.tf7.rt.ResultList;

public class DVgetValue {

	public static void getValue(String[] dvmTable, ResultList result, int returnVal, Container container)
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

			IFIdentifier vmsetsrc = XIVMFactory.newIdentifier("http://grundfos.com/vmrset", senderAgency, senderScheme);
			IFIdentifier vmsetdst = XIVMFactory.newIdentifier("http://grundfos.com/vmrset", receiverAgency,
					receiverScheme);
			String vmset = XIVMService.executeMapping(vmsetsrc, vmsetdst, "0.0.VMRSET");
			context = "http://grundfos.com/vmr/" + vmset;
			trace.addInfo("Class DVinitIDOC: VMR Set: " + context);

		} catch (Exception e) {

			result.addValue("");

		}

		String gcVMKeyL1 = "";
		String gcVMKeyL2 = "";
		String gcVMKeyL3 = "";
		String gcVMKeyL4 = "";

		gcVMKeyL1 = "" + globalContainer.getParameter("vmKeyL1");
		gcVMKeyL2 = "" + globalContainer.getParameter("vmKeyL2");
		gcVMKeyL3 = "" + globalContainer.getParameter("vmKeyL3");
		gcVMKeyL4 = "" + globalContainer.getParameter("vmKeyL4");

		try {
			if (gcVMKeyL1.equals("null") || gcVMKeyL1.length() == 0) {

				gcVMKeyL1 = container.getInputHeader().getConversationId();
			}
		} catch (Exception e) {

			gcVMKeyL1 = container.getInputHeader().getConversationId();
		}

		IFIdentifier src = XIVMFactory.newIdentifier(context, senderAgency, senderScheme);
		IFIdentifier dst = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme);

		String vmKey = "";
		String vmReturn = "";
		String vmReturn1 = "";
		String vmReturn2 = "";
		String vmReturn3 = "";
		String resultVM = "";

		IFIdentifier source = XIVMFactory.newIdentifier(context, senderAgency, senderScheme);
		IFIdentifier destination1 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme1);
		IFIdentifier destination2 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme2);
		IFIdentifier destination3 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme3);

		if (gcVMKeyL4.length() > 0) {

			try {

				vmKey = dvmTable[0] + delimiter + gcVMKeyL1 + delimiter + delimiter + delimiter + delimiter + delimiter
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

				String vmReturnL1[] = vmReturn.split("\\" + delimiter);

				if ((returnVal > vmReturnL1.length) || (returnVal < 1)) {

					trace.addInfo("VM Key L1: " + vmKey);
					resultVM = "";

				} else {

					trace.addInfo("VM Key L1: " + vmKey);
					resultVM = vmReturnL1[returnVal - 1];

				}

				trace.addInfo("Get Value 0: " + resultVM);
				result.addValue(resultVM);

			} catch (ValueMappingException epc) {

				trace.addInfo("VM Key L1 for Table " + dvmTable[0] + " not found. Trying VM Key L2.");

				try {

					String vmkeypg = dvmTable[0] + delimiter + gcVMKeyL2 + delimiter + delimiter + delimiter
							+ delimiter + delimiter + delimiter;
					vmReturn = XIVMService.executeMapping(src, dst, vmkeypg);
					String vmReturnL2[] = vmReturn.split("\\" + delimiter);

					if ((returnVal > vmReturnL2.length) || (returnVal < 1)) {

						trace.addInfo("VM Key L2: " + vmkeypg);
						resultVM = "";

					} else {

						trace.addInfo("VM Key L2: " + vmkeypg);
						resultVM = vmReturnL2[returnVal - 1];

					}

					trace.addInfo("Get Value 0: " + resultVM);
					result.addValue(resultVM);

				} catch (ValueMappingException epg) {

					trace.addInfo("VM Key L2 for Table " + dvmTable[0] + " not found. Trying VM Key L3.");

					try {

						String vmkeygc = dvmTable[0] + delimiter + gcVMKeyL3 + delimiter + delimiter + delimiter
								+ delimiter + delimiter + delimiter;
						vmReturn = XIVMService.executeMapping(src, dst, vmkeygc);
						String vmReturnL3[] = vmReturn.split("\\" + delimiter);

						if ((returnVal > vmReturnL3.length) || (returnVal < 1)) {

							trace.addInfo("VM Key L3: " + vmkeygc);
							resultVM = "";

						} else {

							trace.addInfo("VM Key L3: " + vmkeygc);
							resultVM = vmReturnL3[returnVal - 1];

						}

						trace.addInfo("Get Value 0: " + resultVM);
						result.addValue(resultVM);

					} catch (ValueMappingException egc) {

						trace.addInfo("VM Key L3 for Table " + dvmTable[0] + " not found. Trying VM Key L4.");

						try {

							String vmkeygg = dvmTable[0] + delimiter + gcVMKeyL4 + delimiter + delimiter + delimiter
									+ delimiter + delimiter + delimiter;

							vmReturn = XIVMService.executeMapping(src, dst, vmkeygg);
							String vmReturnL4[] = vmReturn.split("\\" + delimiter);

							if ((returnVal > vmReturnL4.length) || (returnVal < 1)) {

								trace.addInfo("VM Key L4: " + vmkeygg);
								resultVM = "";

							} else {

								trace.addInfo("VM Key L4: " + vmkeygg);
								resultVM = vmReturnL4[returnVal - 1];
							}

							trace.addInfo("Get Value 0: " + resultVM);
							result.addValue(resultVM);

						} catch (ValueMappingException egg) {

							trace.addInfo("VM Key L4 for Table " + dvmTable[0] + "  not found. Conversion failed. "
									+ egg);
							result.addValue("");

						}

					}

				}

			}

		} else {

			trace.addInfo("Map not initialized. Aborting conversion.");
			result.addValue("");

		}
	}

}
