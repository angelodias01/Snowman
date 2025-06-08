package pt.ipbeja.estig.po2.snowman.app.model;

/**
 * The View interface defines a contract for classes responsible for presenting 
 * the game's board state to the user and updating the visual representation when changes occur.
 *
 * This interface is typically implemented by classes that handle the graphical or textual
 * display of the game board. It separates the view logic from the model, adhering to the 
 * Model-View-Controller (MVC) design pattern.
 *
 * Responsibilities:
 * - Ensure the game board's visual representation remains in sync with the underlying model's state.
 * - Provide an implementation for dynamically updating the board when the game state changes.
 *
 * Methods:
 * - {@link #updateBoard()}: Triggered whenever the board's state changes, forcing
 *   the implementing class to refresh the display.
 *
 * Usage:
 * - Classes implementing this interface should be observers of the game's model, 
 *   ensuring that updates to the model propagate to the view in real-time.
 *
 * @version 1.0
 * @since 2025-06-08
 */
public interface View {

    /**
     * Updates the representation of the game board.
     *
     * This method is called whenever the model's board state changes, 
     * requiring the corresponding view to refresh its display to reflect the new state.
     * Implementing classes should redraw or re-render their representation of the board here.
     */
    void updateBoard();
}