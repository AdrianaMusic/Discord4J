/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */
package discord4j.gateway.json.dispatch;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import discord4j.gateway.json.response.VoiceStateResponse;

public class VoiceStateUpdateDispatch implements Dispatch {

    @JsonUnwrapped
    private VoiceStateResponse voiceState;

    public VoiceStateResponse getVoiceState() {
        return voiceState;
    }

    @Override
    public String toString() {
        return "VoiceStateUpdateDispatch{" +
                "voiceState=" + voiceState +
                '}';
    }
}
