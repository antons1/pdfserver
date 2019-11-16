const Path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const TerserPlugin = require('terser-webpack-plugin');
const webpack = require('webpack');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin');

function isProduction() {
  return process.env.NODE_ENV === 'production';
}

const cssExtractLoader = { loader: isProduction() ? MiniCssExtractPlugin.loader : 'style-loader' };
const cssLoader = { loader: "css-loader", options: { sourceMap: true } };
const sassLoader = { loader: "sass-loader", options: { sourceMap: true } };

module.exports = {
  mode: process.env.NODE_ENV || 'production',
  devtool: "source-map",
  entry: "./src/index.tsx",
  output: {
    path: Path.resolve(__dirname, 'dist/'),
    filename: isProduction() ? "[name].[contenthash].js" : "[name].[hash].js",
    chunkFilename: isProduction() ? "[name].[contenthash].js" : "[name].[hash].js"
  },
  plugins: [
    new webpack.ProgressPlugin(),
    new HtmlWebpackPlugin({
      title: "PDF-server",
      template: Path.resolve(__dirname, 'src/templates/index.html')
    }),
    new MiniCssExtractPlugin({
      name: "[name].[contenthash].css",
      chunkFilename: "[name].[contenthash].css"
    })
  ],
  optimization: {
    runtimeChunk: 'single',
    splitChunks: {
      chunks: 'all'
    }
  },
  
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: [ "ts-loader" ]
      },
      {
        test: /\.scss$/,
        use: [ cssExtractLoader, cssLoader, sassLoader ]
      },
      {
        test: /\.css$/,
        use: [ cssExtractLoader, cssLoader ]
      },
      {
        test: /\.(png|svg|jpe?g)$/,
        use: [ "file-loader" ]
      },
      {
        test: /\.(ttf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
        use: [ "file-loader" ]
      }
    ]
  },
  resolve: {
    extensions: [ ".ts", ".tsx", ".js", ".jsx", ".css", ".scss" ]
  }
};

if (process.env.NODE_ENV === 'development') {
  module.exports.devServer = {
    host: "localhost",
    port: process.env.PORT || 4000,
    overlay: true,
    hot: true,
    compress: true
  }
}

if (isProduction()) {
  module.exports.optimization.minimizer = [
    new TerserPlugin(),
    new OptimizeCSSAssetsPlugin()
  ]
}