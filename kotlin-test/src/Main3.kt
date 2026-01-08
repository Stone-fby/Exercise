// 定义二叉树节点
class TreeNode(
    var `val`: Int = 0,
    var left: TreeNode? = null,
    var right: TreeNode? = null
)

// 翻转二叉树的函数
fun invertTree(root: TreeNode?): TreeNode? {
    if (root == null) return null
    swap(root)
    invertTree(root.left)
    invertTree(root.right)
    return root
}

// 交换左右子树
private fun swap(node: TreeNode) {
    val temp = node.left
    node.left = node.right
    node.right = temp
}

// 辅助函数：中序遍历打印树（用于验证）
fun inorderTraversal(root: TreeNode?): List<Int> {
    val result = mutableListOf<Int>()
    fun dfs(node: TreeNode?) {
        if (node == null) return
        dfs(node.left)
        result.add(node.`val`)
        dfs(node.right)
    }
    dfs(root)
    return result
}

// 主函数：用于测试
fun main() {
    // 构建如下二叉树：
    //       4
    //      / \
    //     2   7
    //    / \ / \
    //   1  3 6  9
    val root = TreeNode(4).apply {
        left = TreeNode(2).apply {
            left = TreeNode(1)
            right = TreeNode(3)
        }
        right = TreeNode(7).apply {
            left = TreeNode(6)
            right = TreeNode(9)
        }
    }

    println("原始树的中序遍历: ${inorderTraversal(root)}")

    invertTree(root)

    println("翻转后树的中序遍历: ${inorderTraversal(root)}")
    // 注意：中序遍历在翻转后会变成“镜像”顺序，如原为 [1,2,3,4,6,7,9]，翻转后为 [9,7,6,4,3,2,1]
}