package dev.bti.starters.models.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SuccessResponse extends Response {
    Object data;

    public SuccessResponse(Boolean success, String message, Object data) {
        super(success, message);
        this.data = data;
    }
}
