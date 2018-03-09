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

public class DVgetValueVarInputs {

	public static void getValueVarInputs(String[] dvmTable, ResultList result, int returnVal, String vmKeyInput1,
			String vmKeyInput2, String vmKeyInput3, String vmKeyInput4, String vmKeyInput5, String vmKeyInput6,
			Container container) throws StreamTransformationException {

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

		String vmKeyL1 = "";
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

				vmKeyL1 = dvmTable[0] + delimiter + gcVMKeyL1 + delimiter + vmKeyInput1 + delimiter + vmKeyInput2
						+ delimiter + vmKeyInput3 + delimiter + vmKeyInput4 + delimiter + vmKeyInput5 + delimiter
						+ vmKeyInput6;

				vmReturn1 = XIVMService.executeMapping(source, destination1, vmKeyL1);
				try {
					// Check if VM Target has part 2
					vmReturn2 = XIVMService.executeMapping(source, destination2, vmKeyL1);
				} catch (Exception evmReturn2) {
					vmReturn2 = "";
				}
				try {
					// Check if VM Target has part 3
					vmReturn3 = XIVMService.executeMapping(source, destination3, vmKeyL1);
				} catch (Exception evmReturn3) {
					vmReturn3 = "";
				}

				vmReturn = vmReturn1 + vmReturn2 + vmReturn3;

				String vmReturnL1[] = vmReturn.split("\\" + delimiter);

				if ((returnVal > vmReturnL1.length) || (returnVal < 1)) {

					trace.addInfo("Class DVgetValue: VM Key L1 - " + vmKeyL1);
					resultVM = "";

				} else {

					trace.addInfo("Class DVgetValue: VM Key L1 - " + vmKeyL1);
					resultVM = vmReturnL1[returnVal - 1];

				}

				trace.addInfo("Class DVgetValue: " + resultVM);
				result.addValue(resultVM);

			} catch (ValueMappingException eL1) {

				trace.addInfo("Class DVgetValue: VM Key L1 for Table " + dvmTable[0] + " not found. Trying VM Key L2.");

				try {

					String vmKeyL2 = dvmTable[0] + delimiter + gcVMKeyL2 + delimiter + vmKeyInput1 + delimiter
							+ vmKeyInput2 + delimiter + vmKeyInput3 + delimiter + vmKeyInput4 + delimiter + vmKeyInput5
							+ delimiter + vmKeyInput6;

					vmReturn1 = XIVMService.executeMapping(source, destination1, vmKeyL2);
					try {
						// Check if VM Target has part 2
						vmReturn2 = XIVMService.executeMapping(source, destination2, vmKeyL2);
					} catch (Exception evmReturn2) {
						vmReturn2 = "";
					}
					try {
						// Check if VM Target has part 3
						vmReturn3 = XIVMService.executeMapping(source, destination3, vmKeyL2);
					} catch (Exception evmReturn3) {
						vmReturn3 = "";
					}

					vmReturn = vmReturn1 + vmReturn2 + vmReturn3;

					String vmReturnL2[] = vmReturn.split("\\" + delimiter);

					if ((returnVal > vmReturnL2.length) || (returnVal < 1)) {

						trace.addInfo("Class DVgetValue: VM Key L2 - " + vmKeyL2);
						resultVM = "";

					} else {

						trace.addInfo("Class DVgetValue: VM Key L2 - " + vmKeyL2);
						resultVM = vmReturnL2[returnVal - 1];

					}

					trace.addInfo("Class DVgetValue: " + resultVM);
					result.addValue(resultVM);

				} catch (ValueMappingException eL2) {

					trace.addInfo("Class DVgetValue: VM Key L2 for Table " + dvmTable[0] + " not found. Trying VM Key L3.");

					try {

						String vmKeyL3 = dvmTable[0] + delimiter + gcVMKeyL3 + delimiter + vmKeyInput1 + delimiter
								+ vmKeyInput2 + delimiter + vmKeyInput3 + delimiter + vmKeyInput4 + delimiter
								+ vmKeyInput5 + delimiter + vmKeyInput6;

						vmReturn1 = XIVMService.executeMapping(source, destination1, vmKeyL3);
						try {
							// Check if VM Target has part 2
							vmReturn2 = XIVMService.executeMapping(source, destination2, vmKeyL3);
						} catch (Exception evmReturn2) {
							vmReturn2 = "";
						}
						try {
							// Check if VM Target has part 3
							vmReturn3 = XIVMService.executeMapping(source, destination3, vmKeyL3);
						} catch (Exception evmReturn3) {
							vmReturn3 = "";
						}

						vmReturn = vmReturn1 + vmReturn2 + vmReturn3;

						String vmReturnL3[] = vmReturn.split("\\" + delimiter);

						if ((returnVal > vmReturnL3.length) || (returnVal < 1)) {

							trace.addInfo("Class DVgetValue: VM Key L3 - " + vmKeyL3);
							resultVM = "";

						} else {

							trace.addInfo("Class DVgetValue: VM Key L3 - " + vmKeyL3);
							resultVM = vmReturnL3[returnVal - 1];

						}

						trace.addInfo("Class DVgetValue: " + resultVM);
						result.addValue(resultVM);

					} catch (ValueMappingException eL3) {

						trace.addInfo("Class DVgetValue: VM Key L3 for Table " + dvmTable[0] + " not found. Trying VM Key L4.");

						try {

							String vmKeyL4 = dvmTable[0] + delimiter + gcVMKeyL4 + delimiter + vmKeyInput1 + delimiter
									+ vmKeyInput2 + delimiter + vmKeyInput3 + delimiter + vmKeyInput4 + delimiter
									+ vmKeyInput5 + delimiter + vmKeyInput6;

							vmReturn1 = XIVMService.executeMapping(source, destination1, vmKeyL4);
							try {
								// Check if VM Target has part 2
								vmReturn2 = XIVMService.executeMapping(source, destination2, vmKeyL4);
							} catch (Exception evmReturn2) {
								vmReturn2 = "";
							}
							try {
								// Check if VM Target has part 3
								vmReturn3 = XIVMService.executeMapping(source, destination3, vmKeyL4);
							} catch (Exception evmReturn3) {
								vmReturn3 = "";
							}

							vmReturn = vmReturn1 + vmReturn2 + vmReturn3;

							String vmReturnL4[] = vmReturn.split("\\" + delimiter);

							if ((returnVal > vmReturnL4.length) || (returnVal < 1)) {

								trace.addInfo("Class DVgetValue: VM Key L4 - " + vmKeyL4);
								resultVM = "";

							} else {

								trace.addInfo("Class DVgetValue: VM Key L4 - " + vmKeyL4);
								resultVM = vmReturnL4[returnVal - 1];
							}

							trace.addInfo("Class DVgetValue: " + resultVM);
							result.addValue(resultVM);

						} catch (ValueMappingException eL4) {

							trace.addInfo("Class DVgetValue: VM Key L4 for Table " + dvmTable[0] + "  not found. Conversion failed. "
									+ eL4);
							result.addValue("");

						}

					}

				}

			}

		} else {

			trace.addInfo("Class DVgetValue: Map not initialized. Aborting conversion.");
			result.addValue("");

		}
	}

}
