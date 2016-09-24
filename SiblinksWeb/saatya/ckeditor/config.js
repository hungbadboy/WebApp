/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.toolbar = 'MyToolbar';
 
	config.toolbar_MyToolbar =
		[
			{ name: 'document', items : ['Source'] },
			{ name: 'clipboard', items : ['Undo', 'Redo'] },
			// '/',
			{ name: 'basicstyles', items : ['Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat'] },
			{ name: 'links', items : [ 'Link','Unlink','Anchor' ] },
			{ name: 'paragraph', items : ['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 
			'-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'] },
			{ name: 'insert', items : ['Image', 'Flash', 'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak', 'Iframe', 'SuperButton'] },
			// '/',
			// { name: 'styles', items : ['Styles', 'Format', 'Font', 'FontSize'] },
			// { name: 'colors', items : ['TextColor', 'BGColor'] },
			// { name: 'tools', items : ['Maximize', 'ShowBlocks', '-'] }
		];
	config.removePlugins = 'elementspath';
};
