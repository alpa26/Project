import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
public class VkIp{
    private final String Token;
    private final UserActor actor;
    private final VkApiClient vk;
    private final TransportClient transportClient;

    public VkIp(String token,Integer userId){
        this.Token=token;
        transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        actor = new UserActor(userId, token);
    }

    public String getGroupInf(String groupId, int count) throws ClientException {
        if(count>1000)
        {
            String g = vk.groups().getMembers(actor).groupId(groupId).count(1000).offset(0).fields(Fields.PHOTO_MAX_ORIG, Fields.BDATE,Fields.CITY,Fields.SEX).executeAsString();
            return vk.groups().getMembers(actor).groupId(groupId).count(1000).offset(0).fields(Fields.PHOTO_MAX_ORIG, Fields.BDATE,Fields.CITY,Fields.SEX).executeAsString().substring(0,g.length()-3) +
                    vk.groups().getMembers(actor).groupId(groupId).count(count-1000).offset(0).fields(Fields.PHOTO_MAX_ORIG, Fields.BDATE,Fields.CITY,Fields.SEX).executeAsString().substring(35);
        }
        return vk.groups().getMembers(actor).groupId(groupId).count(count).offset(0).fields(Fields.PHOTO_MAX_ORIG, Fields.BDATE,Fields.CITY,Fields.SEX).executeAsString();
    }

    public String getToken() {
        return Token;
    }

    public TransportClient getTransportClient() {
        return transportClient;
    }
}