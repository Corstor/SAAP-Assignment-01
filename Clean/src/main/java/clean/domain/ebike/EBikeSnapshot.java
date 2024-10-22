package clean.domain.ebike;

import clean.domain.P2d;
import clean.domain.V2d;

public record EBikeSnapshot(String id, EBikeState state, int batteryLevel, 
double speed, V2d direction, P2d location) {
}
