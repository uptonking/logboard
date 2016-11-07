package com.hgt.es;

import com.hgt.utils.DateHelper;
import com.hgt.utils.StringHelper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * INTRO: ES与创建与管理相关的操作，如创建index, type, document
 * Created by root on 11/2/16.
 * ============================================================================
 * CHANGELOG:
 */
public class ESAdminOperations implements Serializable {

    private String hostList = "";
    private TransportClient client = null;
    IndicesAdminClient indicesAdminClient = null;

    public ESAdminOperations(ESConfig esConfig) {
        this.client = esConfig.createESClient();

        AdminClient adminClient = client.admin();
        this.indicesAdminClient = adminClient.indices();

    }

    public ESAdminOperations(String strClusterName, String strHost) {
        this.hostList = strHost;
        this.client = new ESConfig(strClusterName, hostList).createESClient();

        AdminClient adminClient = client.admin();
        this.indicesAdminClient = adminClient.indices();
    }


    public void addIndex(String strIndexName) {
        indicesAdminClient.prepareCreate(strIndexName).get();
    }

    public void addIndex(String strIndexName, int intNumberOfShards, int intNumberOfReplicas) {
        indicesAdminClient
                .prepareCreate(strIndexName)
                .setSettings(Settings.builder()
                        .put("index.number_of_shards", intNumberOfShards)
                        .put("index.number_of_replicas", intNumberOfReplicas)
                )
                .get();
    }

    /**
     * 添加以JSON形式表示的type类型到已有索引
     *
     * @param strTypeName
     * @param strTypeJson
     */
    public void addType(String strIndexName, String strTypeName, String strTypeJson) {

        indicesAdminClient.preparePutMapping(strIndexName)
                .setType(strTypeName)
                .setSource(strTypeJson)
                .get();
    }

    /**
     * 添加简单KV形式表示type类型到已有索引
     *
     * @param strIndexAndType 用逗号分隔，作为一个参数是为了能够重载
     * @param strFieldKV
     */
    public void addType(String strIndexAndType, String strFieldKV) {

        List<HashMap<String, String>> tlist = StringHelper.getListFromString(strFieldKV);
        String typeKV = "";

        for (HashMap<String, String> t : tlist) {
            typeKV += t.get("kk") + ":{\"type\":" + t.get("vv") + "},";
        }
        typeKV = typeKV.substring(0, typeKV.length() - 1);

        String[] indexAndType = strIndexAndType.split(",");
        indicesAdminClient.preparePutMapping(indexAndType[0])
                .setType(indexAndType[1])
                .setSource("{\n" +
                        "  \"properties\": {\n" +
                        typeKV +
                        "  }\n" +
                        "}")
                .get();
    }

    /**
     * 通过纯JSON与type匹配的方式建立索引，数据来源格式必须固定
     *
     * @param strIndexName
     * @param strTypename
     * @param strDataJson
     */
    public void indexingDataByPureJson(String strIndexName, String strTypename, String strDataJson) {

        String dataId = DateHelper.getSimpleDate().replace(" ", "").replace("-", "").replace(":", "");
        IndexResponse response = client.prepareIndex(strIndexName, strTypename, dataId)
                .setSource(strDataJson)
                .get();
    }


    public void deleteIndex(String strIndexName) {
        indicesAdminClient.prepareDelete(strIndexName).get();
    }

    ///TO-DO:直接对HashMap类型的数据建立索引
    public void indexingDataByHashMap(String strIndexName, String strTypeName, HashMap<String, String> mapData) {

    }

    ///TO-DO:动态添加新字段
    public void addNewField(String strIndex, String strType, String strNewField) {

    }

    ///TO-DO:自动关闭
    public void close() {
        this.client.close();
    }

}