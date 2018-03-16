输入：
	1.各部分之间用单个空格分割，指令其他位置不允许有空格, 触发器类型和操作类型的首字符可以大写也可以小写
	2.輸入方式采用指导书的第二种方式，即路径名用方括号括出。全部监控指令输入结束后，需输入end以说明已经结束输入
	3.例如：IF [C:\Users\Persister\Desktop\download] renamed THEN record_detail

输出：
	1.不合法输入会提示"Invalid input!"并忽略本行。
	2.相同输入自动忽略但不提示。
	3.Summary操作保存到eclipse当前项目目录下的Summary.txt，每10s输出一次。
	4.Detail操作保存信息到eclipse当前项目目录下的Summary.txt。
	5.结束输入后，可能会在控制台输出一些其他调试信息，但不会影响程序执行。

测试：
测试者在TestThread类的main函数中使用TestSafeFile类中提供的方法进行测试。所有全部为静态方法，无需实例化文件对象即可使用，文件路径以String类型传入。

boolean creatfile(String path)					// 创建一个路径为path的文件，如果创建成功返回true，否则返回false。
boolean delete(String path) 					// 删除一个路径为path的文件，如果删除成功返回true，否则返回false。
boolean append(String path, String content) 	// 向路径为path的文件中写入字符串content，如果成功则返回true， 否则返回false。
boolean rename(String path, String nname)		// 将路径为path的文件的文件名修改为nname，nname是新文件的文件名，两次修改路径不变，成功则返回true，否则返回false。
boolean changepath(String path, String newpath) //更改路径

