package com.project.expensetracker.util;

import com.project.expensetracker.entity.CategoryLabel;

public class CategoryUtil {
	
	public static CategoryLabel mapCategoryLabel(String label) {
		
		CategoryLabel categoryLabel;
			
	switch (label) {
	case "HOME_UTILITIES":
		categoryLabel = CategoryLabel.HOME_UTILITIES;
		break;
	case "DINING":
		categoryLabel = CategoryLabel.DINING;		
		break;
	case "GROCERIES":
		categoryLabel = CategoryLabel.GROCERIES;
		break;
	case "SHOPPING":
		categoryLabel = CategoryLabel.SHOPPING;		
		break;
	case "ENTERTAINMENT":
		categoryLabel = CategoryLabel.ENTERTAINMENT;
		break;
	case "TRAVEL":
		categoryLabel = CategoryLabel.TRAVEL;		
		break;

	default:
		categoryLabel = CategoryLabel.MISC;
		break;
	}
		return categoryLabel;
		
	}

}
