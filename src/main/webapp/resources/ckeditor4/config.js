/**
 * @license Copyright (c) 2003-2018, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here.
	// For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	// For complete reference see:
	// http://docs.ckeditor.com/#!/api/CKEDITOR.config

	// The toolbar groups arrangement, optimized for two toolbar rows.
	config.toolbar = [
        ['Undo', 'Redo'],
        ['Bold', 'Italic', 'Underline', 'Strike'],
        ['Font', 'FontSize'],
        ['BGColor', 'TextColor' ],
        ['SpecialChar'],
        ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],
        ['Maximize']
    ];
	config.toolbarGroups = [
		{ name: 'tools' }
	];
	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	config.enterMode =CKEDITOR.ENTER_BR;
	config.shiftEnterMode = CKEDITOR.ENTER_P;
	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';
	config.font_names = '굴림체; 굴림; 맑은 고딕; 돋움; 돋움체; 바탕; 돋음; 궁서; Nanum Gothic Coding; Quattrocento Sans;' + CKEDITOR.config.font_names; 
	// Simplify the dialog windows.
	config.removeDialogTabs = 'image:advanced;link:advanced';
	config.colorButton_enableAutomatic = true;
	config.removeButtons = 'Subscript,Superscript';

};
