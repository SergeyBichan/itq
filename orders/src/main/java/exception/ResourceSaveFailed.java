package exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceSaveFailed extends RuntimeException{
    public ResourceSaveFailed(String message) {
        super(message);
        log.error(message);
    }
}
