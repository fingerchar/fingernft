/**
 * @Author YangJun
 * @CreateTime 2020-04-08 10:17
 * @Description
 * @Project index/print_admin
 */
import Clipboard from "@/directive/clipboard";
import Permission from "@/directive/permission";

export default {
  install(app) {
    app.directive("clipboard", Clipboard);
    app.directive("permission", Permission);
  },
};
