package challenge.fiap.service;

import challenge.fiap.models.FAILURE_TYPE;
import challenge.fiap.models.Failure;

import java.util.Arrays;

public class FailureService {

    public static boolean createFailureCheck(Failure failure) {
        return failure.getDescription() != null &&
                Arrays.stream(FAILURE_TYPE.values()).toList().contains(failure.getFailureType());
    }
}
