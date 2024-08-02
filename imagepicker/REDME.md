## 说明


### ImagedDataSource CursorLoader

在 Android 开发中，CursorLoader 是一个方便的工具类，用于在后台线程中异步加载数据，并将结果返回给主线程，以便在 UI 上更新显示。它是 Loader 类的一个子类，特别用于与内容提供者（Content Providers）进行交互，并返回一个 Cursor 对象，通常用于从 SQLite 数据库中查询数据。

主要作用

	1.	异步数据加载：CursorLoader 在后台线程中加载数据，避免阻塞主线程，从而保持应用的响应性。
	2.	自动管理生命周期：CursorLoader 会自动与 LoaderManager 绑定，并管理数据加载过程中的生命周期事件，如创建、启动、停止和重置。
	3.	数据变化自动通知：当底层数据发生变化时，CursorLoader 会自动重新查询并更新数据，简化了数据变化处理的工作。

基本使用步骤

	1.	创建 Loader：在 LoaderManager.LoaderCallbacks 接口的 onCreateLoader 方法中创建 CursorLoader 实例。
	2.	查询数据：通过 CursorLoader 的查询方法从内容提供者中查询数据。
	3.	处理数据：在 LoaderManager.LoaderCallbacks 接口的 onLoadFinished 方法中处理加载完成的数据。
	4.	重置数据：在 LoaderManager.LoaderCallbacks 接口的 onLoaderReset 方法中重置数据。