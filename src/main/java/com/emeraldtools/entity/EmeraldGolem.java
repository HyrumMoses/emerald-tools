package com.emeraldtools.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.OfferFlowerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

// A bigger iron golem: same health, knockback resistance and village behavior,
// twice the attack damage and movement speed, and it also goes after creepers.
public class EmeraldGolem extends IronGolem {
	public static final double MOVEMENT_SPEED = 2 * 0.25;
	public static final double ATTACK_DAMAGE = 2 * 15.0;
	private static final float EMERALD_HEAL_AMOUNT = 25.0F;

	public EmeraldGolem(EntityType<? extends EmeraldGolem> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return IronGolem.createAttributes()
			.add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED)
			.add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE);
	}

	// Same goals as the iron golem, except the monster target goal doesn't skip creepers.
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
		this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 32.0F));
		this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 0.6, false));
		this.goalSelector.addGoal(4, new GolemRandomStrollInVillageGoal(this, 0.6));
		this.goalSelector.addGoal(5, new OfferFlowerGoal(this));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new DefendVillageTargetGoal(this));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (target, level) -> target instanceof Enemy));
		this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	@Override
	protected void doPush(Entity entity) {
		if (entity instanceof Enemy && this.getRandom().nextInt(20) == 0) {
			this.setTarget((LivingEntity) entity);
		}

		// Skip IronGolem.doPush, which would re-run the creeper-excluding check above.
		entity.push(this);
	}

	// IronGolem.canAttack refuses creepers, so this repeats the Mob and LivingEntity checks without that.
	@Override
	public boolean canAttack(LivingEntity target) {
		if (this.isPlayerCreated() && target.is(EntityType.PLAYER)) {
			return false;
		}
		if (target.is(EntityType.GHAST)) {
			return false;
		}
		if (target instanceof Player && this.level().getDifficulty() == Difficulty.PEACEFUL) {
			return false;
		}
		return target.canBeSeenAsEnemy();
	}

	// Repaired with emeralds instead of iron ingots.
	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (!itemStack.is(Items.EMERALD)) {
			return InteractionResult.PASS;
		}

		float healthBefore = this.getHealth();
		this.heal(EMERALD_HEAL_AMOUNT);
		if (this.getHealth() == healthBefore) {
			return InteractionResult.PASS;
		}

		float pitch = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
		this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, pitch);
		itemStack.consume(1, player);
		return InteractionResult.SUCCESS;
	}
}
