module.exports = {
    root: true,
    parserOptions: {
      parser: "babel-eslint",
      ecmaVersion: 2021,
      sourceType: "module",
    },
    parser: "vue-eslint-parser",
    env: {
      browser: true,
      node: true,
      es6: true,
    },
    rules: {
      'no-console': 'off',
    }
};