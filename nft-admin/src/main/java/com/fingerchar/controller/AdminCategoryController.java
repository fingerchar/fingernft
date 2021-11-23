package com.fingerchar.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.annotation.RequiresPermissionsDesc;
import com.fingerchar.base.controller.BaseController;
import com.fingerchar.domain.FcNftCategory;
import com.fingerchar.service.FcCategoryService;
import com.fingerchar.utils.ResponseUtil;

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
			return ResponseUtil.badArgument();
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
			return ResponseUtil.badArgument();
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
			return ResponseUtil.badArgument();
		}
		FcNftCategory fcNftCategory = categoryService.findById(id);
		if (fcNftCategory == null) {
			return ResponseUtil.NotFoud();
		}
		categoryService.deleteById(fcNftCategory);
		return ResponseUtil.ok();
	}

}
