package gui;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.JFrame;

public class BarChartExample extends JFrame {

    public BarChartExample(String title) {
        super(title);

        // Tạo dataset
        DefaultCategoryDataset dataset = createDataset();

        // Tạo biểu đồ cột
        JFreeChart chart = ChartFactory.createBarChart(
            "Biểu đồ doanh thu theo quý", // Tiêu đề biểu đồ
            "Quý", // Nhãn trục X
            "Doanh thu (triệu VND)", // Nhãn trục Y
            dataset, // Dữ liệu
            PlotOrientation.VERTICAL, // Hướng biểu đồ
            true, // Hiển thị legend
            true, // Hiển thị tooltips
            false // Hiển thị URLs
        );

        // Tạo panel chứa biểu đồ
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Thêm dữ liệu mẫu
        dataset.addValue(1000.0, "Sản phẩm A", "Q1");
        dataset.addValue(2000.0, "Sản phẩm A", "Q2");
        dataset.addValue(4000.0, "Sản phẩm A", "Q3");
        dataset.addValue(5000.0, "Sản phẩm A", "Q4");

        dataset.addValue(10000.0, "Sản phẩm B", "Q1");
        dataset.addValue(2577.0, "Sản phẩm B", "Q2");
        dataset.addValue(2341.0, "Sản phẩm B", "Q3");
        dataset.addValue(4235.0, "Sản phẩm B", "Q4");

        return dataset;
    }

    public static void main(String[] args) {
        BarChartExample chart = new BarChartExample("Biểu đồ cột doanh thu");
        chart.setSize(800, 600);
        chart.setLocationRelativeTo(null);
        chart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chart.setVisible(true);
    }
}