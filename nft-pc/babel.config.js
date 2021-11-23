module.exports = {
  presets: [
    ['@vue/app', {
      polyfills: [
        'es.promise',
        'es.symbol'
      ]
    }]
    // '@vue/cli-plugin-babel/preset'
  ],
  plugins: [
      [
          'import', {
              libraryName: 'iview',
              libraryDirectory: 'es'
           },
          'iview'
      ],
    [
        'import', {
         libraryName: 'vant',
        libraryDirectory: 'es',
        style: true
         },
      'vant']
  ],

};
