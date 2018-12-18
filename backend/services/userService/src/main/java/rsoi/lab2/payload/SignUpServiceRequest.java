package rsoi.lab2.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

public class SignUpServiceRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @NotBlank
    @Size(min = 6, max = 20)
    private UUID uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
