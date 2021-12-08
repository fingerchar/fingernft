<template>
  <div class="language-body">
      <span>{{$t('language.choose')}}:</span>
    <el-select @change="change" v-model="value" class="select" :placeholder="$t('language.choose')">
      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
    </el-select>
  </div>
</template>
<script>
export default {
  data() {
    return {
      value: "",
      options: [
        {
          value: "en",
          label: 'English'
        },
        {
          value: "cn",
          label: '中文'
        },
      ],
    };
  },
  methods: {
    change() {
      if (this.value == "en") {
        localStorage.setItem("locale", "en");
        this.$i18n.locale = localStorage.getItem("locale");
      } else if (this.value == "cn") {
        localStorage.setItem("locale", "zh");
        this.$i18n.locale = localStorage.getItem("locale");
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.language-body{
    width:100%;
    height:100%;
    padding-left:50px;
    .select{
        margin:50px 0 0 10px;
    }
}
</style>