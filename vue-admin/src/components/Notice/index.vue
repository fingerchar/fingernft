<template>
  <div>
    <el-badge :is-dot="hasNotice">
      <el-icon @click="click" style="font-size: 20px; cursor: pointer"
        ><Bell
      /></el-icon>
    </el-badge>
    <audio ref="audioRef" preload="preload" src="/prompt.mp3" />
  </div>
</template>

<script>
  // import { nNotice, readAll } from '@/api/profile'

  export default {
    data() {
      return {
        hasNotice: false,
        timer: "",
      };
    },
    computed: {},
    mounted() {},
    beforeUnmount() {
      clearInterval(this.timer);
      this.$refs.audioRef.pause();
    },
    // created() {
    //   this.checkNotice();
    //   this.handleVisibilityChange = this.handleVisibilityChange.bind(this);
    //   this.checkNotice = this.checkNotice.bind(this);
    //   document.addEventListener(
    //     "visibilitychange",
    //     this.handleVisibilityChange,
    //     false
    //   );
    //   this.timer = setInterval(this.checkNotice, 1000 * 60);
    // },
    methods: {
      click() {
        this.$store.commit("SET_NOTICE", false);
        // if (this.$route.path === '/profile/notice') return
        this.$router.push({ path: "/profile/notice" });
      },
      // checkNotice() {
      //   nNotice().then(response => {
      //     this.hasNotice = (response && response.data && response.data > 0) || false
      //     this.hasNotice && !document.hidden && this.$nextTick(() => {
      //       this.$refs.audioRef.play()
      //       readAll()
      //     })
      //     this.$store.commit('SET_NOTICE', this.hasNotice)
      //   })
      // },
      handleVisibilityChange() {
        if (document.hidden && this.$refs.audioRef)
          this.$refs.audioRef && this.$refs.audioRef.pause();
      },
    },
  };
</script>

<style lang="scss" scoped>
  .el-badge__content.is-fixed.is-dot {
    right: 5px;
    top: 10px;
  }
</style>
