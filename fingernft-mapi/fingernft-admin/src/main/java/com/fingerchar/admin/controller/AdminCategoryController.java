package com.fingerchar.admin.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcNftCategory;
import com.fingerchar.admin.service.FcCategoryService;

@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController extends BaseController {

	@Autowired
	private FcCategoryService categoryService;

	@RequiresPermissions("admin:category:list")
	@RequiresPermissionsDesc(menu = { "分类管理", "分类列表" }, button = "查询")
	@PostMapping("/list")
	public Object list() {
		IPage<FcNftCategory> iPage = categoryService.queryAllCategory(this.getPageInfo());
		return ResponseUtil.okList(iPage);
	}

	private Object validate(FcNftCategory category) {
		String name = category.getName();
		if (StringUtils.isEmpty(name)) {
			return ResponseUtil.fail(-1, "NFT类型名称不能为空！");
		}
		return null;
	}

	@RequiresPermissions("admin:category:create")
	@RequiresPermissionsDesc(menu = { "分类管理", "分类列表" }, button = "创建")
	@PostMapping("/create")
	public Object create(FcNftCategory category) {
		Object error = validate(category);
		if (error != null) {
			return error;
		}
		categoryService.add(category);
		return ResponseUtil.ok(category);
	}

	@RequiresPermissions("admin:category:update")
	@RequiresPermissionsDesc(menu = { "分类管理", "分类列表" }, button = "更新")
	@PostMapping("/update")
	public Object update(FcNftCategory category) {
		Object error = validate(category);
		if (error != null) {
			return error;
		}

		if (categoryService.updateById(category) == 0) {
			return ResponseUtil.updatedDataFailed();
		}
		return ResponseUtil.ok();
	}

	@RequiresPermissions("admin:category:disable")
	@RequiresPermissionsDesc(menu = { "分类管理", "分类列表" }, button = "禁用")
	@PostMapping("/disable")
	public Object delete(Long id) {
		if (id == null) {
			return ResponseUtil.fail(-1, "种类ID不能为空！");
		}
		FcNftCategory fcNftCategory = categoryService.findById(id);
		if (fcNftCategory == null) {
			return ResponseUtil.NotFoud();
		}
		categoryService.deleteById(fcNftCategory);
		return ResponseUtil.ok();
	}

	@RequiresPermissions("admin:category:enable")
	@RequiresPermissionsDesc(menu = { "分类管理", "分类列表" }, button = "启用")
	@PostMapping("/enable")
	public Object enable(Long id) {
		if (id == null) {
			return ResponseUtil.fail(-1, "种类ID不能为空！");
		}
		FcNftCategory fcNftCategory = categoryService.findById(id);
		if (fcNftCategory == null) {
			return ResponseUtil.NotFoud();
		}
		categoryService.deleteById(fcNftCategory);
		return ResponseUtil.ok();
	}

}
