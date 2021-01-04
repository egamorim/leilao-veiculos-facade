package com.egamorim.mgz.auction.legacy;

import java.util.List;

public interface LegacyGateway {

    VehicleLegacyDTO callLegacyAPI(VehicleLegacyDTORequest vehicle);

    List<VehicleLegacyDTO> callLegacyAPIGet(VehicleLegacyDTORequest vehicle);
}
