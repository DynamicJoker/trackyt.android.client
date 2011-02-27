package trackyt.android.client;

import trackyt.android.client.adapter.ApiV11Adapter;

public class TrackytApiAdapterFactory {
	public static TrackytApiAdapter createV11Adapter() {
		return new ApiV11Adapter();
	}
}
