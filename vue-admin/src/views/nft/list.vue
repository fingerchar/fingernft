<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.creator"
        clearable
        class="filter-item w-200 mr-10"
        :placeholder="$t('nftManagement.placeholder1')"
      />
      <el-input
        v-model="listQuery.address"
        clearable
        class="filter-item w-200 mr-10"
        :placeholder="$t('nftManagement.placeholder2')"
      />
      <el-input
        v-model="listQuery.tokenId"
        clearable
        class="filter-item w-200 mr-10"
        :placeholder="$t('nftManagement.placeholder3')"
      />
      <!-- <el-input v-model="listQuery.nftVerify" clearable class="filter-item w-200 mr-10" :placeholder="$t('nftManagement.placeholder4')" /> -->
      <el-select
        v-model="listQuery.nftVerify"
        class="filter-item w-200 mr-10"
        :placeholder="$t('nftManagement.placeholder4')"
        :clearable="true"
      >
        <el-option
          v-for="item in nftVerifys"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button class="filter-item" type="primary" @click="handleFilter"
        ><el-icon><Search /></el-icon
        >{{ $t("nftManagement.select") }}</el-button
      >
    </div>
    <!-- 查询结果 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      :element-loading-text="$t('nftManagement.loadingText')"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        align="center"
        :label="$t('nftManagement.name')"
        prop="name"
        width="100px"
      >
        <template #default="scope">
          <span>{{ scope.row.metadataContent.name }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('nftManagement.authenticationStatus')"
        prop="nftVerify"
        width="150"
      >
        <template #default="scope">
          <el-tag type="info" v-if="scope.row.nftVerify == 0">{{
            $t("nftManagement.unauthenticated")
          }}</el-tag>
          <el-tag type="success" v-else-if="scope.row.nftVerify == 1">{{
            $t("nftManagement.certificationPassed")
          }}</el-tag>
          <el-tag type="danger" v-else>{{
            $t("nftManagement.authenticationFailed")
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('nftManagement.nftAddress')"
        prop="address"
      />
      <el-table-column
        align="center"
        :label="$t('nftManagement.creator')"
        prop="creator"
      />
      <el-table-column
        align="center"
        label="tokenId"
        prop="tokenId"
        width="100px"
      />
      <el-table-column
        align="center"
        :label="$t('nftManagement.picture')"
        prop="metadataContent"
        width="150"
      >
        <template #default="scope">
          <media
            v-if="scope.row.metadataContent.image"
            :isPreview="true"
            :url="scope.row.metadataContent.image"
            type="image"
          ></media>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('nftManagement.operation')"
        width="300"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            type="primary"
            size="small"
            @click="handleDetail(scope.row)"
            >{{ $t("nftManagement.details") }}</el-button
          >
          <el-button
            v-permission="['POST /admin/nft/verify']"
            type="success"
            size="small"
            @click="(verifyForm = true), (selectNft = scope.row)"
            v-if="scope.row.nftVerify != 1"
          >
            {{ $t("nftManagement.immediateCertification") }}</el-button
          >
          <el-button
            v-permission="['POST /admin/nft/verify']"
            type="success"
            size="small"
            @click="cancelCertification(scope.row)"
            v-else
            >{{ $t("nftManagement.cancelCertification") }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="listQuery.page"
      v-model:limit="listQuery.limit"
      @pagination="getList"
    />
    <!-- 对话框 -->
    <el-dialog :title="$t('nftManagement.details')" v-model="dialogFormVisible">
      <el-table
        v-loading="listLoading"
        :data="details"
        :element-loading-text="$t('nftManagement.loadingText')"
        border
        fit
        highlight-current-row
      >
        <el-table-column align="center" label="ID" prop="id" width="100px" />
        <el-table-column
          align="center"
          :label="$t('nftManagement.name')"
          prop="name"
          width="100px"
        />
        <el-table-column
          align="center"
          :label="$t('nftManagement.createTime')"
          prop="createTime"
          width="100px"
        >
          <template #default="scope">
            <span>{{ formatDate(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          :label="$t('nftManagement.onsellStatus')"
          prop="onsell"
          width="100px"
        >
          <template #default="scope">
            <el-tag type="danger" v-if="!scope.row.onsell">{{
              $t("nftManagement.stopSelling")
            }}</el-tag>
            <el-tag type="success" v-else>{{
              $t("nftManagement.onSale")
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          :label="$t('nftManagement.isSync')"
          prop="isSync"
          width="100px"
        >
          <template #default="scope">
            <span type="danger" v-if="scope.row.isSync">{{
              $t("nftManagement.yes")
            }}</span>
            <span type="success" v-else>{{ $t("nftManagement.no") }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          :label="$t('nftManagement.itemOwner')"
          prop="itemOwner"
          width="100px"
        />
        <el-table-column
          align="center"
          :label="$t('nftManagement.price')"
          prop="usdtPrice"
          width="100px"
        />
        <el-table-column
          align="center"
          :label="$t('nftManagement.picture')"
          prop="metadataContent"
        >
          <template #default="scope">
            <media
              v-if="scope.row.metadataContent.image"
              :isPreview="true"
              :url="scope.row.metadataContent.image"
              type="image"
            ></media>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog :title="$t('nftManagement.authentication')" v-model="verifyForm">
      <el-radio v-model="verifyStatus" :label="1">{{
        $t("nftManagement.certificationPassed")
      }}</el-radio>
      <el-radio v-model="verifyStatus" :label="2">{{
        $t("nftManagement.authenticationFailed")
      }}</el-radio>
      <br />
      <div class="verifyClass">
        <el-button type="info" size="small" @click="verifyForm = false">{{
          $t("util.cancel")
        }}</el-button>
        <el-button type="success" size="small" @click="verifyFunction">{{
          $t("util.confirm")
        }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import Pagination from "@/components/Pagination";
  import Media from "@/components/Media";
  export default {
    components: { Pagination, Media },
    data() {
      return {
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 10,
          creator: undefined,
          tokenId: undefined,
          address: undefined,
          nftVerify: undefined,
          sort: "create_time",
          order: "desc",
        },
        downloadLoading: false,
        dialogFormVisible: false,
        sellState: "",
        nftStyle: "",
        details: [],
        verifyForm: false,
        verifyStatus: 1,
        selectNft: {},
        ipfsUrl: "",
        nftVerifys: [
          {
            value: 0,
            label: this.$t("nftManagement.unauthenticated"),
          },
          {
            value: 1,
            label: this.$t("nftManagement.certificationPassed"),
          },
          {
            value: 2,
            label: this.$t("nftManagement.authenticationFailed"),
          },
        ],
      };
    },
    computed: {
      formatDate() {
        return function (createTime) {
          createTime = new Date(createTime * 1000);
          let create_date = createTime.toLocaleDateString();
          let create_time = createTime.toTimeString().slice(0, 8);
          return create_date + " " + create_time;
        };
      },
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        this.listLoading = true;
        this.$api("nft.list", this.listQuery)
          .then((response) => {
            this.formatData(response.data.list);
            this.list = response.data.list;
            this.total = response.data.total;
            this.listLoading = false;
          })
          .catch(() => {
            this.list = [];
            this.total = 0;
            this.listLoading = false;
          });
      },
      formatData(data) {
        for (let i = 0, len = data.length; i < len; i++) {
          if (data[i].metadataContent) {
            data[i].metadataContent = JSON.parse(data[i].metadataContent);
          } else {
            data[i].metadataContent = { image: "", animation_url: "" };
          }
        }
      },
      handleFilter() {
        this.listQuery.page = 1;
        this.getList();
      },
      handleDetail(row) {
        this.$api("nft.detail", { address: row.address, tokenId: row.tokenId })
          .then((response) => {
            if (this.$tool.checkResponse(response)) {
              response.data.metadataContent = row.metadataContent;
              response.data.name = row.metadataContent.name;
              this.details = [response.data];
              this.dialogFormVisible = true;
            }
          })
          .catch((response) => {
            this.$notify.error({
              title: this.$t("util.errors"),
              message: this.$t("response." + response.data.errno),
            });
          });
      },
      verifyFunction() {
        this.listLoading = true;
        this.$api("nft.verify", {
          address: this.selectNft.address,
          tokenId: this.selectNft.tokenId,
          nftVerify: this.verifyStatus,
        })
          .then(() => {
            this.$notify.success({
              title: this.$t("authentication.success"),
              message: this.$t("authentication.completed"),
            });
            this.verifyForm = false;
            this.getList();
          })
          .catch((response) => {
            this.$notify.error({
              title: this.$t("authentication.fail"),
              message: this.$t("response." + response.data.errno),
            });
            this.verifyForm = false;
          });
      },
      cancelCertification(row) {
        let that = this;
        this.$confirm(this.$t("util.tipContent1"), this.$t("util.tip"), {
          confirmButtonText: this.$t("util.confirm"),
          cancelButtonText: this.$t("util.cancel"),
          type: "warning",
        }).then(() => {
          that
            .$api("nft.verify", {
              address: row.address,
              tokenId: row.tokenId,
              nftVerify: 0,
            })
            .then((response) => {
              if (that.$tool.checkResponse(response)) {
                that.$notify.success({
                  title: this.$t("authentication.success"),
                  message: this.$t("authentication.completed"),
                });
                that.verifyForm = false;
                that.getList();
              }
            })
            .catch((response) => {
              that.$notify.error({
                title: this.$t("authentication.fail"),
                message: this.$t("response." + response.data.errno),
              });
              that.verifyForm = false;
            });
        });
      },
    },
  };
</script>
<style scoped>
  .mb-20 {
    margin-bottom: 20px;
  }
  .table-cell {
    padding: 10px;
    font-size: 14px;
    text-align: center;
    overflow: hidden;
  }
  .title {
    font-style: 16px;
    font-weight: 700;
  }
  .width-70px {
    width: 70px;
  }
  .centerClass {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .verifyClass {
    width: 100%;
    display: flex;
    justify-content: flex-end;
  }
</style>
