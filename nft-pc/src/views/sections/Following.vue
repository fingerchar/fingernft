<template>
  <div>
    <div class="g-main">
      <div class="m-top">
        <div class="m-top-title">{{$t('following.following')}}</div>
        <filter-and-sort :filters="filters" @selectFilter="selectFilter"></filter-and-sort>
      </div>
      <div class="m-list">
        <div class="new-following-list" v-infinite-scroll="loadfollowingList">
          <div class="following">
            <nft-item v-for="(following, i) in followingList" :nft="following" :index="i"
              @clickLike="clickLike" :likeData='likeData' @onLike="onLike" :key="i">
            </nft-item>
            <nft-item-load :loadStatus="followingLoadStatus"></nft-item-load>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>
<script>
import FilterAndSort from "@/components/FilterAndSort";
import NftItem from '@/mixins/NftItem';
export default {
  components: {
    FilterAndSort,
  },
  mixins: [NftItem],
  data: function () {
    return {
      filters: [
        "Art",
        "Photography",
        "Memes",
        "Games",
        "Domains",
        "Music",
        "DeFi",
        "Metaverses",
      ],
      followingList: [],
      followingQuery: {
        page: 1,
        limit: this.$store.state.pageLimit,
        address: this.$store.state.user.coinbase,
        categoryId: null,
        filterId: "",
      },
      followingLoadStatus: "",
      temLike: [],
      likeData: [],
      cateId: 1,
      categoryIndex: 0,
      sorts: "price",
      order: "desc",
    };
  },
  created() {
    this.sort();
    this.init();
  },
  mounted() {},
  computed: {},
  methods: {
    selectFilter(filterId) {
      this.followingQuery.filterId =
        this.followingQuery.filterId == filterId ? "" : filterId;
      this.followingQuery.page = 1;
      this.getfollowingList();
    },
    setNftLikes(likes) {
      for (var i = 0; i < this.followingList.length; i++) {
        let nft = this.followingList[i];

        for (var l = 0; l < likes.length; l++) {
          let like = likes[l];
          like = like.split(":");
          if (nft.nft.address == like[0] && nft.nft.tokenId == like[1]) {
            this.followingList[i].like = true;
          }
        }
      }
    },
    queryLike(nfts) {
      let _nfts = [];
      for (var i = 0; i < nfts.length; i++) {
        let nft = nfts[i];
        _nfts.push(nft.nft.address + ":" + nft.nft.tokenId);
      }
      _nfts = _nfts.join(",");
      _nfts = { nfts: _nfts };

      this.$api("like.list", _nfts).then((res) => {
        if (this.$tools.checkResponse(res)) {
          this.setNftLikes(res.data);
        } else {
          this.$tools.message(res.errmsg);
        }
      });
    },
    sort() {
      this.$api("category.list").then((res) => {
        this.filters = res.data;
      });
    },
    selectFilter(parameter) {
      this.followingQuery.categoryId = parameter;
      this.getfollowingList();
    },
    onLike(index, like) {
      this.followingList[index].like = like;
    },
    init() {
      this.getfollowingList();
    },
    loadfollowingList() {
      if (this.followingLoadStatus == "over") return;

      this.getfollowingList();
    },
    getfollowingList() {
      var data = {
        ...this.followingQuery,
      };
      if (this.followingLoadStatus == "loading") return;
      this.followingLoadStatus = "loading";
      this.$api("home.following", data).then((res) => {
        if (this.$tools.checkResponse(res)) {
          if (data.page == 1) this.followingList = [];
          this.followingList = this.followingList.concat(res.data.list);
          this.queryFunction(res.data.list);
          if (res.data.list.length < data.limit) {
            this.followingLoadStatus = "over";
          } else {
            this.followingQuery.page += 1;
            this.followingLoadStatus = "";
          }
        } else {
          this.$tools.message(res.errmsg);
        }
      });
    },
    clickLike(i) {
      var id = this.followingList[i].good.id;
      let data = {
        id: id,
      };
      this.$api("following.like", data).then((res) => {
        if (this.tools.checkResponse(res)) {
          if (this.temLike[i] == true) {
            this.temLike[i] = false;
          } else {
            this.temLike[i] = true;
          }
        } else {
          this.$tools.message(res.errmsg);
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.g-main {
  padding: 0 30px;
  padding-top: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.m-top {
  width: 100%;

  display: flex;
  justify-content: space-between;
  align-items: center;
}
.m-top-title {
  font-size: 24px;
  font-weight: 900;
}
.f-flex {
  display: flex;
}
.m-none {
  width: 100%;
  height: 100%;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 122px;
  margin-bottom: 182px;
}
.m-none-text {
  margin-top: 23px;
  font-size: 17px;
  font-family: Montserrat-Regular;
  font-weight: 400;
  color: #1b1b1b;
  line-height: 8px;
}
.following {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}
.m-list {
  margin-top: 28px;
  width: 100%;
}
.loading-status {
  padding-bottom: 50px;
}
.following-loading {
  height: 60px;
}
.following-loading-over {
  text-align: center;
  padding: 30px 0;
  color: $grayColor;
}
</style>
