package com.zebia.loaders.params;

import android.os.Bundle;

public interface ParamsMapper {
    Bundle map(String ip, String port, String mountpoint, RestParamBuilder restParamBuilder);
}
